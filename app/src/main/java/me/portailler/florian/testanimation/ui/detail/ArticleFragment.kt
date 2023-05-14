package me.portailler.florian.testanimation.ui.detail

import android.content.Context
import android.location.GnssAntennaInfo.Listener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialContainerTransform
import me.portailler.florian.testanimation.ArticleEntity
import me.portailler.florian.testanimation.databinding.ArticleFragmentBinding
import me.portailler.florian.testanimation.loadAndSetImage
import me.portailler.florian.testanimation.ui.shared.ArticlesViewModel
import me.portailler.florian.testanimation.ui.shared.BaseFragment

class ArticleFragment : BaseFragment<ArticleFragmentBinding>() {

	companion object {

		private const val ARG_TRANSITION_NAME = "ARG_TRANSITION_NAME"

		fun build(transitionName: String): ArticleFragment = ArticleFragment().apply {
			sharedElementEnterTransition = MaterialContainerTransform()
			arguments = bundleOf(ARG_TRANSITION_NAME to transitionName)
		}
	}

	private val viewModel: ArticlesViewModel by activityViewModels()
	private val transitionName: String? by lazy { arguments?.getString(ARG_TRANSITION_NAME, null) }
	private val enterCallback by lazy {
		object : SharedElementCallback() {
			override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
				super.onMapSharedElements(names, sharedElements)
				if (transitionName == null) return
				if (sharedElements == null) return
				if (names.isNullOrEmpty()) return
				val element = sharedElements[names.first()]?:return
				binding.articleImage.transitionName = transitionName
				ViewCompat.animate(element).start()
			}
		}
	}

	override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): ArticleFragmentBinding {
		return ArticleFragmentBinding.inflate(layoutInflater, container, false)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return super.onCreateView(inflater, container, savedInstanceState).also {
			sharedElementEnterTransition = MaterialContainerTransform()

			setEnterSharedElementCallback(enterCallback)
			postponeEnterTransition()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		lifecycleScope.launchWhenCreated {
			viewModel.currentArticle.collect(::onArticleLoaded)
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		activity?.onBackPressedDispatcher?.addCallback {
			viewModel.closeCurrentArticle()
		}
	}

	private fun onArticleLoaded(article: ArticleEntity?) {
		// if null, let the activity close the fragment
		if (article == null) return

		//else do the binding
		binding.articleImage.transitionName = "#${article.id}"
		binding.articleImage.loadAndSetImage(article.imageUrl, onImageReady = ::startPostponedEnterTransition)
		binding.articleMeta.text = article.author
		binding.articleContent.text = article.content
		binding.articleTitle.text = article.title
	}

}