package me.portailler.florian.testanimation.ui.modale

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import me.portailler.florian.testanimation.databinding.ModalFragmentBinding
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_FULL_HEIGHT
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.setHandleBehavior
import me.portailler.florian.testanimation.ui.shared.BaseFragment

open class ModalFragment : BaseFragment<ModalFragmentBinding>() {
	companion object {

		private const val ARG_FLAGS = "ModalFragment.ARG_FLAGS"
		fun newInstance(
			flags: Int = FLAG_FULL_HEIGHT
		): ModalFragment = ModalFragment().apply {
			arguments = bundleOf(
				ARG_FLAGS to flags
			)
		}
	}

	private var draggedOutListener: ((ModalFragment) -> Unit)? = null
	protected open val flags: Int = FLAG_FULL_HEIGHT

	override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): ModalFragmentBinding {
		return ModalFragmentBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		createContentView(binding.modalFragmentContainer)?.let(binding.modalFragmentContainer::addView)
		binding.modalFragmentHandle.setHandleBehavior(
			binding.root,
			onDragRelease = ::onDragRelease,
			onDraggedOut = ::onDraggedOut,
			flags = flags
		)
		binding.root.isVisible = true
	}

	open fun createContentView(rootView: ViewGroup): View? = null

	fun setOnDraggedOutListener(listener: (ModalFragment) -> Unit) {
		draggedOutListener = listener
	}

	private fun onDraggedOut() {
		Log.d("ModalFragment", "onDraggedOut")
		draggedOutListener?.invoke(this)
	}

	private fun onDragRelease(level: ModalUtils.DragLevel) {
		Log.d("ModalFragment", "onDragRelease $level")
	}

}
