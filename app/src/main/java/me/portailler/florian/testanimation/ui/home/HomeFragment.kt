package me.portailler.florian.testanimation.ui.home

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialContainerTransform
import me.portailler.florian.testanimation.ArticleEntity
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.databinding.ArticleItemBinding
import me.portailler.florian.testanimation.databinding.HomeFragmentBinding
import me.portailler.florian.testanimation.ui.detail.ArticleFragment
import me.portailler.florian.testanimation.ui.shared.ArticlesViewModel
import me.portailler.florian.testanimation.ui.shared.BaseFragment
import java.lang.ref.WeakReference

class HomeFragment : BaseFragment<HomeFragmentBinding>() {

	private val viewModel: ArticlesViewModel by activityViewModels()
	private val articlesAdapter by lazy { ArticlesAdapter(this::onArticleClick) }
	private val exitCallback by lazy {
		object : SharedElementCallback() {
			override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
				if (names.isNullOrEmpty()) return
				val article = viewModel.currentArticle.value ?: return
				val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(article.id) as? ArticlesAdapter.ArticleItem ?: return
				sharedElements?.put(names.first(), viewHolder.getImageView())
			}
		}
	}
	private var articleFragment: WeakReference<ArticleFragment>? = null

	override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): HomeFragmentBinding {
		return HomeFragmentBinding.inflate(inflater, container, false)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return super.onCreateView(inflater, container, savedInstanceState).also {
			exitTransition = MaterialContainerTransform()

			setExitSharedElementCallback(exitCallback)
			exitTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.article_image_transition)
			postponeEnterTransition()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.recyclerView.adapter = articlesAdapter

		lifecycleScope.launchWhenCreated {
			viewModel.articles.collect(::onArticlesLoaded)
		}
		lifecycleScope.launchWhenCreated {
			viewModel.currentArticle.collect(::onCurrentArticleUpdate)
		}
		viewModel.loadAllArticles()
	}


	private fun onArticlesLoaded(articles: List<ArticleEntity>) {
		articlesAdapter.replaceAll(articles)
	}

	private fun onArticleClick(view: ArticleItemBinding, article: ArticleEntity) {
		viewModel.setCurrentArticle(article)
		if (childFragmentManager.backStackEntryCount == 0) displayArticle(view.articleImage)
	}

	private fun displayArticle(articleImageView: ImageView) {
		val fragment = ArticleFragment.build(articleImageView.transitionName)
		articleFragment = WeakReference(fragment)
		childFragmentManager.beginTransaction()
			.addSharedElement(articleImageView, articleImageView.transitionName)
			.add(binding.homeFragmentContainer.id, fragment)
			.commit()
	}

	private fun onCurrentArticleUpdate(article: ArticleEntity?) {
		if (article != null) return
		articleFragment?.get()?.let {
			childFragmentManager.beginTransaction()
				.remove(it)
				.commit()
		}
	}
}