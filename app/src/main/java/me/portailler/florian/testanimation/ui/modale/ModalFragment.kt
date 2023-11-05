package me.portailler.florian.testanimation.ui.modale

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.portailler.florian.testanimation.databinding.ModalFragmentBinding
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_FULL_HEIGHT
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_HALF_HEIGHT
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_LOW_HEIGHT
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_MINIMIZABLE
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.setHandleBehavior
import me.portailler.florian.testanimation.ui.shared.BaseFragment

open class ModalFragment : BaseFragment<ModalFragmentBinding>() {
	companion object {

		fun newInstance(): ModalFragment = ModalFragment()
	}

	private var draggedOutListener: ((ModalFragment) -> Unit)? = null

	override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): ModalFragmentBinding {
		return ModalFragmentBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setContentView(binding.modalFragmentContainer)
		binding.modalFragmentHandle.setHandleBehavior(
			binding.root,
			onDragRelease = ::onDragRelease,
			onDraggedOut = ::onDraggedOut,
			flags = FLAG_FULL_HEIGHT or FLAG_HALF_HEIGHT or FLAG_LOW_HEIGHT or FLAG_MINIMIZABLE
		)
		binding.root.isVisible = true
	}

	open fun setContentView(rootView: View) = Unit

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
