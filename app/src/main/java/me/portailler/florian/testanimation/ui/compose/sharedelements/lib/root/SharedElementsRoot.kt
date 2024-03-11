package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.computation.MathUtils.calculateDirection
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.PathMotion
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.tracker.SharedElementTransition
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.tracker.SharedElementsTracker
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.SharedElementsTransitionSpec

@Composable
internal fun BaseSharedElement(
	elementInfo: SharedElementInfo,
	isFullscreen: Boolean,
	placeholder: @Composable () -> Unit,
	overlay: @Composable (SharedElementsTransitionState) -> Unit,
	content: @Composable (Modifier) -> Unit
) {
	val (savedShouldHide, setShouldHide) = remember { mutableStateOf(false) }
	val rootState = LocalSharedElementsRootState.current
	val shouldHide = rootState.onElementRegistered(elementInfo)
	setShouldHide(shouldHide)

	val compositionLocalContext = currentCompositionLocalContext
	if (isFullscreen) {
		rootState.onElementPositioned(
			elementInfo,
			compositionLocalContext,
			placeholder,
			overlay,
			null,
			setShouldHide
		)

		Spacer(modifier = Modifier.fillMaxSize())
	} else {
		val contentModifier = Modifier
			.onGloballyPositioned { coordinates ->
				rootState.onElementPositioned(
					elementInfo,
					compositionLocalContext,
					placeholder,
					overlay,
					coordinates,
					setShouldHide
				)
			}
			.run {
				if (shouldHide || savedShouldHide) alpha(0f) else this
			}

		content(contentModifier)
	}

	DisposableEffect(elementInfo) {
		onDispose {
			rootState.onElementDisposed(elementInfo)
		}
	}
}

@Composable
public fun SharedElementsRoot(
	content: @Composable SharedElementsRootScope.() -> Unit
) {
	val rootState = remember { SharedElementsRootState() }

	Box(modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
		rootState.rootCoordinates = layoutCoordinates
		rootState.rootBounds = Rect(Offset.Zero, layoutCoordinates.size.toSize())
	}) {
		CompositionLocalProvider(
			LocalSharedElementsRootState provides rootState,
			LocalSharedElementsRootScope provides rootState.scope
		) {
			rootState.scope.content()
			UnboundedBox { SharedElementTransitionsOverlay(rootState) }
		}
	}

	DisposableEffect(Unit) {
		onDispose {
			rootState.onDispose()
		}
	}
}

@Composable
private fun UnboundedBox(content: @Composable () -> Unit) {
	Layout(content) { measurables, constraints ->
		val infiniteConstraints = Constraints()
		val placeables = measurables.fastMap {
			val isFullscreen = it.layoutId === FullscreenLayoutId
			it.measure(if (isFullscreen) constraints else infiniteConstraints)
		}
		layout(constraints.maxWidth, constraints.maxHeight) {
			placeables.fastForEach { it.place(0, 0) }
		}
	}
}

@Composable
private fun SharedElementTransitionsOverlay(rootState: SharedElementsRootState) {
	rootState.recomposeScope = currentRecomposeScope
	rootState.trackers.forEach { (key, tracker) ->
		key(key) {
			val transition = tracker.transition
			val start = (tracker.state as? SharedElementsTracker.State.StartElementPositioned)?.startElement
			if (transition != null || (start != null && start.bounds == null)) {
				val startElement = start ?: transition!!.startElement
				val startScreenKey = startElement.info.screenKey
				val endElement = (transition as? SharedElementTransition.InProgress)?.endElement
				val spec = startElement.info.spec
				val animated = remember(startScreenKey) { Animatable(0f) }
				val fraction = animated.value
				startElement.info.onFractionChanged?.invoke(fraction)
				endElement?.info?.onFractionChanged?.invoke(1 - fraction)

				val direction = if (endElement == null) null else remember(startScreenKey) {
					val direction = spec.direction
					if (direction != TransitionDirection.Auto) direction else
						calculateDirection(
							startElement.bounds ?: rootState.rootBounds!!,
							endElement.bounds ?: rootState.rootBounds!!
						)
				}

				startElement.Placeholder(
					rootState, fraction, endElement,
					direction, spec, tracker.pathMotion
				)

				if (transition is SharedElementTransition.InProgress) {
					LaunchedEffect(transition, animated) {
						repeat(spec.waitForFrames) { withFrameNanos {} }
						animated.animateTo(
							targetValue = 1f,
							animationSpec = tween(
								durationMillis = spec.durationMillis,
								delayMillis = spec.delayMillis,
								easing = spec.easing
							)
						)
						transition.onTransitionFinished()
					}
				}
			}
		}
	}
}

@Composable
internal fun PositionedSharedElement.Placeholder(
	rootState: SharedElementsRootState,
	fraction: Float,
	end: PositionedSharedElement? = null,
	direction: TransitionDirection? = null,
	spec: SharedElementsTransitionSpec? = null,
	pathMotion: PathMotion? = null
) {
	overlay(
		SharedElementsTransitionState(
			fraction = fraction,
			startInfo = info,
			startBounds = if (end == null) bounds else bounds ?: rootState.rootBounds,
			startCompositionLocalContext = compositionLocalContext,
			startPlaceholder = placeholder,
			endInfo = end?.info,
			endBounds = end?.run { bounds ?: rootState.rootBounds },
			endCompositionLocalContext = end?.compositionLocalContext,
			endPlaceholder = end?.placeholder,
			direction = direction,
			spec = spec,
			pathMotion = pathMotion
		)
	)
}


enum class TransitionDirection {
	Auto, Enter, Return
}

internal class SharedElementsTransitionState(
	val fraction: Float,
	val startInfo: SharedElementInfo,
	val startBounds: Rect?,
	val startCompositionLocalContext: CompositionLocalContext,
	val startPlaceholder: @Composable () -> Unit,
	val endInfo: SharedElementInfo?,
	val endBounds: Rect?,
	val endCompositionLocalContext: CompositionLocalContext?,
	val endPlaceholder: (@Composable () -> Unit)?,
	val direction: TransitionDirection?,
	val spec: SharedElementsTransitionSpec?,
	val pathMotion: PathMotion?
)

internal val TopLeft = TransformOrigin(0f, 0f)

internal open class SharedElementInfo(
	val key: Any,
	val screenKey: Any,
	val spec: SharedElementsTransitionSpec,
	val onFractionChanged: ((Float) -> Unit)?
) {

	final override fun equals(other: Any?): Boolean =
		other is SharedElementInfo && other.key == key && other.screenKey == screenKey

	final override fun hashCode(): Int = 31 * key.hashCode() + screenKey.hashCode()

}


internal val Fullscreen = Modifier.fillMaxSize()
internal val FullscreenLayoutId = Any()
