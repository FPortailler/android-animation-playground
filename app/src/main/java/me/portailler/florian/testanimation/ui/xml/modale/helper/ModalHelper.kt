package me.portailler.florian.testanimation.ui.xml.modale.helper

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.FLAG_FULL_HEIGHT
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.FLAG_FULL_SCREEN
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.FLAG_HALF_HEIGHT
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.FLAG_LOW_HEIGHT
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.FLAG_MINIMIZABLE
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.checkFlagValidity
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.setHandleBehavior
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.swipeOut

@Suppress("unused")
open class ModalHelper<T : Fragment>(
	private val getHandle: T.() -> View,
	private val getModalView: T.() -> View = { view!! },
	private val getFullscreeenCloseButton: (T.() -> View)? = null,
	private val onDraggedOut: () -> Unit = {},
	private val onDragRelease: (ModalUtils.DragLevel) -> Unit = {},
) {

	protected open var flags: Int = FLAG_FULL_HEIGHT
	var fullscreen: Boolean
		get() = flags and FLAG_FULL_SCREEN == FLAG_FULL_SCREEN
		set(value) {
			flags = if (value) FLAG_FULL_SCREEN
			else flags and (1 shl FLAG_FULL_SCREEN).inv()
		}

	var fullHeight: Boolean
		get() = flags and FLAG_FULL_HEIGHT == FLAG_FULL_HEIGHT
		set(value) {
			flags = if (value) flags or FLAG_FULL_HEIGHT
			else flags and (1 shl FLAG_FULL_HEIGHT).inv()
		}

	var midHeight: Boolean
		get() = flags and FLAG_HALF_HEIGHT == FLAG_HALF_HEIGHT
		set(value) {
			flags = if (value) flags or FLAG_HALF_HEIGHT
			else flags and (1 shl FLAG_HALF_HEIGHT).inv()
		}

	var lowHeight: Boolean
		get() = flags and FLAG_LOW_HEIGHT == FLAG_LOW_HEIGHT
		set(value) {
			flags = if (value) flags or FLAG_LOW_HEIGHT
			else flags and (1 shl FLAG_LOW_HEIGHT).inv()
		}

	var minimizeEnabled: Boolean
		get() = flags and FLAG_MINIMIZABLE == FLAG_MINIMIZABLE
		set(value) {
			flags = if (value) flags or FLAG_MINIMIZABLE
			else flags and (1 shl FLAG_MINIMIZABLE).inv()
		}

	@Throws(IllegalStateException::class)
	protected open fun setFullscreenBehavior(fragment: T) {
		val closeButton = getFullscreeenCloseButton?.invoke(fragment)
		check(closeButton != null) { "Close button view is required for flag ModalUtils.FLAG_FULL_SCREEN" }
		getHandle(fragment).isVisible = false
		closeButton.isVisible = true
		closeButton.setOnClickListener {
			val rootView = getModalView(fragment)
			rootView.swipeOut(rootView.height, rootView.y, onAnimationCompleted = onDraggedOut)
		}
	}


	@Throws(IllegalStateException::class)
	fun attachTo(fragment: T) {
		checkFlagValidity(flags)
		when {
			ModalUtils.isFullscreen(flags) -> setFullscreenBehavior(fragment)
			else -> getHandle(fragment).setHandleBehavior(
				getModalView(fragment),
				onDraggedOut = onDraggedOut,
				onDragRelease = onDragRelease,
				flags = flags,
			)
		}
	}
}
