package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.toSize
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.tracker.SharedElementsTracker


internal class SharedElementsRootState {
	private val choreographer = ChoreographerWrapper()
	val scope: SharedElementsRootScope = Scope()
	var trackers by mutableStateOf(mapOf<Any, SharedElementsTracker>())
	var recomposeScope: RecomposeScope? = null
	var rootCoordinates: LayoutCoordinates? = null
	var rootBounds: Rect? = null

	fun onElementRegistered(elementInfo: SharedElementInfo): Boolean {
		choreographer.removeCallback(elementInfo)
		return getTracker(elementInfo).onElementRegistered(elementInfo)
	}

	fun onElementPositioned(
		elementInfo: SharedElementInfo,
		compositionLocalContext: CompositionLocalContext,
		placeholder: @Composable () -> Unit,
		overlay: @Composable (SharedElementsTransitionState) -> Unit,
		coordinates: LayoutCoordinates?,
		setShouldHide: (Boolean) -> Unit
	) {
		val element = PositionedSharedElement(
			info = elementInfo,
			compositionLocalContext = compositionLocalContext,
			placeholder = placeholder,
			overlay = overlay,
			bounds = coordinates?.calculateBoundsInRoot()
		)
		getTracker(elementInfo).onElementPositioned(element, setShouldHide)
	}

	fun onElementDisposed(elementInfo: SharedElementInfo) {
		choreographer.postCallback(elementInfo) {
			val tracker = getTracker(elementInfo)
			tracker.onElementUnregistered(elementInfo)
			if (tracker.isEmpty) trackers = trackers - elementInfo.key
		}
	}

	fun onDispose() {
		choreographer.clear()
	}

	private fun getTracker(elementInfo: SharedElementInfo): SharedElementsTracker {
		return trackers[elementInfo.key] ?: SharedElementsTracker { transition ->
			recomposeScope?.invalidate()
			(scope as Scope).isRunningTransition = if (transition != null) true else
				trackers.values.any { it.transition != null }
		}.also { trackers = trackers + (elementInfo.key to it) }
	}

	private fun LayoutCoordinates.calculateBoundsInRoot(): Rect =
		Rect(
			rootCoordinates?.localPositionOf(this, Offset.Zero)
				?: positionInRoot(), size.toSize()
		)

	private inner class Scope : SharedElementsRootScope {

		override var isRunningTransition: Boolean by mutableStateOf(false)
		override var selectedIndex: Int by mutableIntStateOf(-1)
		override var previousSelectedIndex: Int by mutableIntStateOf(-1)

		override fun prepareTransition(vararg elements: Any) {
			elements.forEach {
				trackers[it]?.prepareTransition()
			}
		}
	}
}


internal val LocalSharedElementsRootState = staticCompositionLocalOf<SharedElementsRootState> {
	error("SharedElementsRoot not found. SharedElement must be hosted in SharedElementsRoot.")
}