package me.portailler.florian.testanimation.ui.modale

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.portailler.florian.testanimation.databinding.ModalFragmentBinding
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
			onDragCancelled = ::onDragCancelled,
			onDraggedOut = ::onDraggedOut
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

	private fun onDragCancelled() {
		Log.d("ModalFragment", "onDragCancelled")
	}

}
