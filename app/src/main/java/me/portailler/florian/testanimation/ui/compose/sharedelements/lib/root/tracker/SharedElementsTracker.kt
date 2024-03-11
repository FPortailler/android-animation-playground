package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.PathMotion
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.PositionedSharedElement
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.SharedElementInfo

internal class SharedElementsTracker(
	private val onTransitionChanged: (SharedElementTransition?) -> Unit
) {
	var state: State = State.Empty

	var pathMotion: PathMotion? = null

	// Use snapshot state to trigger recomposition of start element when transition starts
	private var _transition: SharedElementTransition? by mutableStateOf(null)
	var transition: SharedElementTransition?
		get() = _transition
		set(value) {
			if (_transition != value) {
				_transition = value
				if (value == null) pathMotion = null
				onTransitionChanged(value)
			}
		}

	val isEmpty: Boolean get() = state is State.Empty

	private fun State.StartElementPositioned.prepareTransition() {
		if (transition !is SharedElementTransition.WaitingForEndElementPosition) {
			transition = SharedElementTransition.WaitingForEndElementPosition(startElement)
		}
	}

	fun prepareTransition() {
		(state as? State.StartElementPositioned)?.prepareTransition()
	}

	fun onElementRegistered(elementInfo: SharedElementInfo): Boolean {
		var shouldHide = false

		val transition = transition
		if (transition is SharedElementTransition.InProgress
			&& elementInfo != transition.startElement.info
			&& elementInfo != transition.endElement.info
		) {
			state = State.StartElementPositioned(startElement = transition.endElement)
			this.transition = null
		}

		when (val state = state) {
			is State.StartElementPositioned -> {
				if (!state.isRegistered(elementInfo)) {
					shouldHide = true
					this.state = State.EndElementRegistered(
						startElement = state.startElement,
						endElementInfo = elementInfo
					)
					state.prepareTransition()
				}
			}

			is State.StartElementRegistered -> {
				if (elementInfo != state.startElementInfo) {
					this.state = State.StartElementRegistered(startElementInfo = elementInfo)
				}
			}

			is State.Empty -> {
				this.state = State.StartElementRegistered(startElementInfo = elementInfo)
			}

			else -> Unit
		}
		return shouldHide || transition != null
	}

	fun onElementPositioned(element: PositionedSharedElement, setShouldHide: (Boolean) -> Unit) {
		val state = state
		if (state is State.StartElementPositioned && element.info == state.startElementInfo) {
			state.startElement = element
			return
		}

		when (state) {
			is State.EndElementRegistered -> {
				if (element.info == state.endElementInfo) {
					this.state = State.InTransition
					val spec = element.info.spec
					this.pathMotion = spec.pathMotionFactory()
					transition = SharedElementTransition.InProgress(
						startElement = state.startElement,
						endElement = element,
						onTransitionFinished = {
							this.state = State.StartElementPositioned(startElement = element)
							transition = null
							setShouldHide(false)
						}
					)
				}
			}

			is State.StartElementRegistered -> {
				if (element.info == state.startElementInfo) {
					this.state = State.StartElementPositioned(startElement = element)
				}
			}

			else -> Unit
		}
	}

	fun onElementUnregistered(elementInfo: SharedElementInfo) {
		when (val state = state) {
			is State.EndElementRegistered -> {
				if (elementInfo == state.endElementInfo) {
					this.state = State.StartElementPositioned(startElement = state.startElement)
					transition = null
				} else if (elementInfo == state.startElement.info) {
					this.state = State.StartElementRegistered(startElementInfo = state.endElementInfo)
					transition = null
				}
			}

			is State.StartElementRegistered -> {
				if (elementInfo == state.startElementInfo) {
					this.state = State.Empty
					transition = null
				}
			}

			else -> Unit
		}
	}

	sealed class State {
		object Empty : State()

		open class StartElementRegistered(val startElementInfo: SharedElementInfo) : State() {
			open fun isRegistered(elementInfo: SharedElementInfo): Boolean {
				return elementInfo == startElementInfo
			}
		}

		open class StartElementPositioned(var startElement: PositionedSharedElement) :
			StartElementRegistered(startElement.info)

		class EndElementRegistered(
			startElement: PositionedSharedElement,
			val endElementInfo: SharedElementInfo
		) : StartElementPositioned(startElement) {
			override fun isRegistered(elementInfo: SharedElementInfo): Boolean {
				return super.isRegistered(elementInfo) || elementInfo == endElementInfo
			}
		}

		object InTransition : State()
	}
}