package me.portailler.florian.testanimation.ui.tinderCompose.box

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
	resetAnimationDuration: Int = 100,
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

	LaunchedEffect(currentPoint) {
		if (rotationAngle != 0f) {
			animatableX.snapTo(currentPoint.x)
			animatableY.snapTo(currentPoint.y)
			animatableRotationAngle.snapTo(rotationAngle)
		}
	}

	LaunchedEffect(startPoint) {
		if (startPoint == Offset.Zero) {
			launch {
				animatableX.animateTo(
					targetValue = 0f,
					animationSpec = tween(
						durationMillis = resetAnimationDuration,
						easing = EaseInOut
					)
				)
			}
			launch {
				animatableY.animateTo(
					targetValue = 0f,
					animationSpec = tween(
						durationMillis = resetAnimationDuration,
						easing = EaseInOut
					),
				)
			}
			launch {
				animatableRotationAngle.animateTo(
					targetValue = 0f,
					animationSpec = tween(
						durationMillis = resetAnimationDuration,
						easing = EaseInBounce
					),
				)
			}
		}
	}

	Box(modifier = modifier) {
		for (index in currentIndex until min(currentIndex + 2, itemCount)) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.onGloballyPositioned {
						size = it.size
					}
					.pointerInput(Unit) {
						detectDragGestures(
							onDragStart = {
								startPoint = it.copy()
								currentPoint = Offset.Zero
							},
							onDragCancel = {
								startPoint = Offset.Zero
							},
							onDragEnd = {
								when {
									currentPoint.x < swipeThreshold * size.width -> onSwipe(DIRECTION_LEFT, index)
									currentPoint.x > (1 - swipeThreshold) * size.width -> onSwipe(DIRECTION_RIGHT, index)

									else -> onSwipe(DIRECTION_NONE, index)
								}
								startPoint = Offset.Zero
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