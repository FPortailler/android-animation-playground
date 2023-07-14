package me.portailler.florian.testanimation.ui.tinderCompose.box

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
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
	build: @Composable (index: Int) -> Unit,
	onSwipe: (direction: Int, index: Int) -> Unit,
	onDrag: (index: Int, progress: Float) -> Unit,
) {
	val currentIndex by rememberSaveable { mutableStateOf(0) }
	var currentPoint by remember { mutableStateOf(Offset.Zero) }
	var startPoint by remember { mutableStateOf(Offset.Zero) }
	var size by remember { mutableStateOf(IntSize.Zero) }
	var rotationAngle by remember { mutableStateOf(0f) }


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
								currentPoint = Offset.Zero
								rotationAngle = 0f
							},
							onDragEnd = {
								when {
									currentPoint.x < swipeThreshold * size.width -> onSwipe(DIRECTION_LEFT, index)
									currentPoint.x > (1 - swipeThreshold) * size.width -> onSwipe(DIRECTION_RIGHT, index)

									else -> onSwipe(DIRECTION_NONE, index)
								}
								rotationAngle = 0f
								startPoint = Offset.Zero
								currentPoint = Offset.Zero
							}
						) { change, dragAmount ->
							change.consume()
							currentPoint += dragAmount
							rotationAngle = currentPoint.x.div(max(1f, startPoint.x)) * tiltEmphasis
						}
					}
					.offset { IntOffset(currentPoint.x.toInt(), currentPoint.y.toInt()) }
					.graphicsLayer(
						transformOrigin = TransformOrigin(
							pivotFractionX = 0.5f,
							pivotFractionY = 1.25f,
						),
						rotationZ = rotationAngle,
//						scaleX = currentPoint.x,
//						scaleY = currentPoint.y,
					)
			) {
				build(index)
			}
		}
	}
}