package me.portailler.florian.testanimation.ui.tinderCompose.box

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBounce
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
	build: @Composable (index: Int) -> Unit,
	onSwipe: (direction: Int, index: Int) -> Unit,
	onDrag: (index: Int, progress: Float) -> Unit,
) {
	val currentIndex by rememberSaveable { mutableStateOf(0) }
	val animatableX = remember { Animatable(0f) }
	val animatableY = remember { Animatable(0f) }
	val animatableRotationAngle = remember { Animatable(0f) }
	var currentPoint by remember { mutableStateOf(Offset.Zero) }
	var startPoint by remember { mutableStateOf(Offset.Zero) }
	var size by remember { mutableStateOf(IntSize.Zero) }
	var rotationAngle by remember { mutableStateOf(0f) }
	var animationState: AnimationState by remember { mutableStateOf(AnimationState.Idle, neverEqualPolicy()) }

	LaunchedEffect(currentPoint) {
		if (rotationAngle != 0f) {
			animatableX.snapTo(currentPoint.x)
			animatableY.snapTo(currentPoint.y)
			animatableRotationAngle.snapTo(rotationAngle)
		}
	}

	LaunchedEffect(animationState) {
		val state = (animationState as? AnimationState.Target) ?: return@LaunchedEffect
		if (state.x != 0f) launch {
			delay(state.duration.toLong())
			animationState = AnimationState.Target(duration = 0)
		}

		launch {
			animatableX.animateTo(
				targetValue = state.x,
				animationSpec = tween(
					durationMillis = state.duration,
					easing = EaseInOut
				)
			)
		}
		launch {
			animatableY.animateTo(
				targetValue = state.y,
				animationSpec = tween(
					durationMillis = state.duration,
					easing = EaseInOut
				),
			)
		}
		launch {
			animatableRotationAngle.animateTo(
				targetValue = state.angle,
				animationSpec = tween(
					durationMillis = state.duration,
					easing = EaseInBounce
				),
			)
		}
	}

	Box(modifier = modifier
		.onGloballyPositioned {
			size = it.size
		}
	) {
		for (index in currentIndex until min(currentIndex + 2, itemCount)) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.pointerInput(Unit) {
						detectDragGestures(
							onDragStart = {
								startPoint = it.copy()
								currentPoint = Offset.Zero
							},
							onDragCancel = {
								Log.d("DragAndSwipeBox", "onDragCancel")
								startPoint = Offset.Zero
								animationState = AnimationState.Target(duration = resetAnimationDuration)
							},
							onDragEnd = {
								val endPoint = (startPoint + currentPoint)
								val direction = when {
									endPoint.x < swipeThreshold * size.width -> DIRECTION_LEFT
									endPoint.x > (1 - swipeThreshold) * size.width -> DIRECTION_RIGHT
									else -> DIRECTION_NONE
								}
								val dx = direction * size.width.toFloat() * 2
								animationState = AnimationState.Target(
									x = dx,
									y = if (dx == 0f) 0f else startPoint.targetY(endPoint, dx),
									angle = if (dx == 0f) 0f else rotationAngle,
									duration = resetAnimationDuration
								)
								startPoint = Offset.Zero
								Log.d("DragAndSwipeBox", "onDragEnd ($animationState)")
							}
						) { change, dragAmount ->
							change.consume()
							currentPoint += dragAmount
							rotationAngle = currentPoint.x.div(max(1f, startPoint.x)) * tiltEmphasis
						}
					}
					.offset {
						IntOffset(
							animatableX.value.toInt(),
							animatableY.value.toInt()
						)
					}
					.graphicsLayer(
						transformOrigin = TransformOrigin(
							pivotFractionX = 0.5f,
							pivotFractionY = 1.25f,
						),
						rotationZ = animatableRotationAngle.value,
					)
			) {
				build(index)
			}
		}
	}
}


private sealed interface AnimationState {
	object Idle : AnimationState
	data class Target(
		val x: Float = 0f,
		val y: Float = 0f,
		val angle: Float = 0f,
		val duration: Int = 0
	) : AnimationState
}

private fun Offset.targetY(offset: Offset, dx: Float): Float {
	val factor = (offset.x - x) / (offset.x + dx - x)
	return (offset.y - y) / factor - (offset.y - y)
}