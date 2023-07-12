package me.portailler.florian.testanimation.ui.sharedelement.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import me.portailler.florian.testanimation.ui.sharedelement.ArticleEntity
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.databinding.ArticleItemBinding
import me.portailler.florian.testanimation.databinding.HomeFragmentBinding
import me.portailler.florian.testanimation.ui.sharedelement.detail.ArticleFragment
import me.portailler.florian.testanimation.ui.shared.ArticlesViewModel
import me.portailler.florian.testanimation.ui.shared.BaseFragment

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

	override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): HomeFragmentBinding {
		return HomeFragmentBinding.inflate(inflater, container, false)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return super.onCreateView(inflater, container, savedInstanceState).also {
			setExitSharedElementCallback(exitCallback)
			postponeEnterTransition()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.recyclerView.adapter = articlesAdapter

		lifecycleScope.launchWhenCreated {
			viewModel.articles.collect(::onArticlesLoaded)
		}
	}

	override fun onResume() {
		super.onResume()
		viewModel.loadAllArticles()
	}


	private fun onArticlesLoaded(articles: List<ArticleEntity>) {
		articlesAdapter.replaceAll(articles)
	}

	private fun onArticleClick(view: ArticleItemBinding, article: ArticleEntity) {
		viewModel.setCurrentArticle(article)
		displayArticle(view.articleImage)
	}

	private fun displayArticle(articleImageView: ImageView) {
		val fragment = ArticleFragment.build(articleImageView.transitionName)
		parentFragmentManager.beginTransaction()
			.setReorderingAllowed(true)
			.addSharedElement(articleImageView, articleImageView.transitionName)
			.replace(R.id.mainFragmentContainer, fragment, ArticleFragment::class.java.simpleName)
			.addToBackStack(ArticleFragment::class.java.simpleName)
			.commit()
	}
}