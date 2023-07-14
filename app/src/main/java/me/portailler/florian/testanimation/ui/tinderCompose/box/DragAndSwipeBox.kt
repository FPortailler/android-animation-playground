package me.portailler.florian.testanimation.ui.tinderCompose.box

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

const val DIRECTION_LEFT = -1
const val DIRECTION_RIGHT = 1
const val DIRECTION_NONE = 0

@Composable
fun DragAndSwipeBox(
	modifier: Modifier = Modifier,
	itemCount: Int,
	tiltEmphasis: Float = 5f,
	swipeThreshold: Float = 0.2f,
	resetAnimationDuration: Int = 300,
	build: @Composable (Modifier, index: Int) -> Unit,
	onSwipe: (direction: Int, index: Int) -> Unit,
	onDrag: (index: Int, progress: Float) -> Unit,
) {
	var currentIndex by rememberSaveable { mutableStateOf(0) }
	val animatableX = remember { Animatable(0f) }
	val animatableY = remember { Animatable(0f) }
	val animatableRotationAngle = remember { Animatable(0f) }
	var size by remember { mutableStateOf(IntSize.Zero) }
	var animationState: AnimationState by remember { mutableStateOf(AnimationState.Idle, neverEqualPolicy()) }

	LaunchedEffect(animationState) {
		when (animationState) {
			is AnimationState.Dragging -> {
				animatableX.snapTo(animationState.x)
				animatableY.snapTo(animationState.y)
				animatableRotationAngle.snapTo(animationState.angle)
			}

			is AnimationState.Reset,
			is AnimationState.SwipeOut -> {
				launch {
					animatableX.animateTo(
						targetValue = animationState.x,
						animationSpec = tween(
							durationMillis = animationState.duration,
							easing = EaseInOut
						)
					)
				}
				launch {
					animatableY.animateTo(
						targetValue = animationState.y,
						animationSpec = tween(
							durationMillis = animationState.duration,
							easing = EaseInOut
						)
					)
				}
				launch {
					animatableRotationAngle.animateTo(
						targetValue = animationState.angle,
						animationSpec = tween(
							durationMillis = animationState.duration,
							easing = EaseInOut
						)
					)
				}
				if (animationState is AnimationState.SwipeOut) launch {
					delay(resetAnimationDuration.toLong())
					currentIndex++
					animationState = AnimationState.Reset(duration = 0)
				}
			}

			else -> Unit
		}
	}

	Box(
		modifier = modifier
			.onGloballyPositioned { size = it.size }
			.fillMaxSize()
	) {
		if (itemCount == 0) return@Box
		for (index in currentIndex..min(currentIndex + 1, itemCount - 1)) {
			build(
				Modifier
					.zIndex(
						kotlin.math
							.abs(currentIndex - index + 2)
							.toFloat()
					)
					.let {
						if (index == currentIndex) it.detectDragAndSwipeGesture(
							state = DragAndSwipeState(
								animatedX = animatableX.value.toInt(),
								animatedY = animatableY.value.toInt(),
								animatedAngle = animatableRotationAngle.value,
								animationState = animationState
							),
							tiltEmphasis = tiltEmphasis,
							swipeThreshold = swipeThreshold,
							resetAnimationDuration = resetAnimationDuration,
							onStateUpdate = { newState -> animationState = newState.animationState },
							onSwipe = { direction -> onSwipe(direction, currentIndex) },
							onDrag = { progress -> onDrag(currentIndex, progress) }
						)
						else it
					},
				index
			)
		}
	}
}

private fun Modifier.detectDragAndSwipeGesture(
	state: DragAndSwipeState,
	tiltEmphasis: Float = 5f,
	swipeThreshold: Float = 0.2f,
	resetAnimationDuration: Int = 300,
	onStateUpdate: (DragAndSwipeState) -> Unit,
	onSwipe: (direction: Int) -> Unit,
	onDrag: (progress: Float) -> Unit,
): Modifier = this
	.pointerInput(Unit) {
		var currentPoint = Offset.Zero
		var startPoint = Offset.Zero
		var rotationAngle = 0f

		detectDragGestures(
			onDragStart = {
				startPoint = it.copy()
				currentPoint = Offset.Zero

			},
			onDragCancel = {
				startPoint = Offset.Zero
				onStateUpdate(
					state.copy(
						animationState = AnimationState.Reset(duration = resetAnimationDuration)
					)
				)
			},
			onDragEnd = {

				val endPoint = (startPoint + currentPoint)
				val direction = when {
					endPoint.x <= swipeThreshold * size.width -> DIRECTION_LEFT
					endPoint.x >= (1 - swipeThreshold) * size.width -> DIRECTION_RIGHT
					else -> DIRECTION_NONE
				}
				val dx = direction * size.width.toFloat() * 2
				onStateUpdate(
					state.copy(
						animationState = if (direction == DIRECTION_NONE) AnimationState.Reset(duration = resetAnimationDuration)
						else AnimationState.SwipeOut(
							x = dx,
							y = startPoint.targetY(endPoint, dx),
							angle = rotationAngle,
							duration = resetAnimationDuration
						),
					)
				)
				onSwipe(direction)
			}
		) { change, dragAmount ->
			change.consume()
			currentPoint += dragAmount
			rotationAngle = currentPoint.x.div(max(1f, startPoint.x)) * tiltEmphasis
			onStateUpdate(
				state.copy(
					animationState = AnimationState.Dragging(
						x = currentPoint.x,
						y = currentPoint.y,
						angle = rotationAngle,
					)
				)
			)
			onDrag((startPoint.x + currentPoint.x) / size.width)
		}
	}
	.offset {
		IntOffset(
			state.animatedX,
			state.animatedY
		)
	}
	.graphicsLayer(
		transformOrigin = TransformOrigin(
			pivotFractionX = 0.5f,
			pivotFractionY = 1.25f,
		),
		rotationZ = state.animatedAngle,
	)


private data class DragAndSwipeState(
	val animatedX: Int,
	val animatedY: Int,
	val animatedAngle: Float,
	val animationState: AnimationState = AnimationState.Idle,
)

private sealed class AnimationState(
	open val x: Float = 0f,
	open val y: Float = 0f,
	open val angle: Float = 0f,
	open val duration: Int = 0,
) {
	object Idle : AnimationState()

	data class Dragging(
		override val x: Float = 0f,
		override val y: Float = 0f,
		override val angle: Float = 0f,
		override val duration: Int = 0,
	) : AnimationState()

	data class SwipeOut(
		override val x: Float = 0f,
		override val y: Float = 0f,
		override val angle: Float = 0f,
		override val duration: Int = 0
	) : AnimationState()

	data class Reset(override val duration: Int) : AnimationState(duration = duration)
}

private fun Offset.targetY(offset: Offset, dx: Float): Float {
	val factor = (offset.x - x) / (offset.x + dx - x)
	return (offset.y - y) / factor - (offset.y - y)
}