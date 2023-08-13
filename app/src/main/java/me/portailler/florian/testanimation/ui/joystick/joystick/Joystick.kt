package me.portailler.florian.testanimation.ui.joystick.joystick

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Joystick(
	modifier: Modifier = Modifier,
	duration: Int = 300,
	onStateUpdate: (JoystickState) -> Unit,
) {

	val ripple = rememberRipple()
	val animatableX = remember { Animatable(0f) }
	val animatableY = remember { Animatable(0f) }
	var positionInRoot by remember { mutableStateOf(Offset.Zero) }
	var animationState: JoystickAnimationState by remember { mutableStateOf(JoystickAnimationState.Idle, neverEqualPolicy()) }


	LaunchedEffect(animationState) {
		when (animationState) {
			is JoystickAnimationState.Dragging -> {
				animatableX.snapTo(animationState.x)
				animatableY.snapTo(animationState.y)
			}

			is JoystickAnimationState.Reset -> {
				launch {
					animatableX.animateTo(
						targetValue = animationState.x,
						animationSpec = tween(
							durationMillis = animationState.duration,
							easing = EaseOutElastic
						)
					)
				}
				launch {
					animatableY.animateTo(
						targetValue = animationState.y,
						animationSpec = tween(
							durationMillis = animationState.duration,
							easing = EaseOutElastic
						)
					)
				}
			}

			else -> Unit
		}
	}

	Box(
		modifier = Modifier
			.then(modifier)
	) {
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.indication(
					interactionSource = MutableInteractionSource(),
					indication = ripple
				)
				.clip(MaterialTheme.shapes.extraLarge)
				.padding(24.dp)
				.clip(MaterialTheme.shapes.extraLarge),
			color = Color.Gray,
		) {}

		Surface(
			modifier = Modifier
				.onGloballyPositioned {
					positionInRoot = it.positionInRoot()
				}
				.fillMaxSize()
				.detectJoystickGesture(
					joystickAnimationState = animationState,
					resetAnimationDuration = duration,
					globalPosition = positionInRoot,
					onJoystickStateUpdate = onStateUpdate,
					onAnimationStateUpdate = { animationState = it },
				)
				.padding(24.dp)
				.padding(8.dp)
				.clip(MaterialTheme.shapes.extraLarge),
			color = Color.Cyan,
		) {}
	}
}

private fun Modifier.detectJoystickGesture(
	joystickAnimationState: JoystickAnimationState,
	resetAnimationDuration: Int = 300,
	globalPosition: Offset,
	onJoystickStateUpdate: (JoystickState) -> Unit,
	onAnimationStateUpdate: (JoystickAnimationState) -> Unit,
): Modifier = this
	.pointerInput(Unit) {
		var currentPoint = Offset.Zero
		val startPoint = Offset(size.width / 2f + globalPosition.x, size.height / 2f + globalPosition.y)
		var rotationAngle = 0f

		detectDragGestures(
			onDragStart = {
				currentPoint = Offset.Zero
			},
			onDragCancel = {
				onAnimationStateUpdate(JoystickAnimationState.Reset(resetAnimationDuration))
				onJoystickStateUpdate(JoystickState(angle = rotationAngle))
			},
			onDragEnd = {
				onAnimationStateUpdate(JoystickAnimationState.Reset(resetAnimationDuration))
				onJoystickStateUpdate(JoystickState(angle = rotationAngle))
			}
		) { change, dragAmount ->
			change.consume()
			val limit = size.width
			val vector = Offset(startPoint.x - (currentPoint.x + dragAmount.x), startPoint.y - (currentPoint.y + dragAmount.y)).reduceNormIfBiggerThan(limit.toFloat())
			currentPoint += if (vector.norm >= limit) Offset.Zero else dragAmount
			Log.d("Joystick", "vector: $vector (norm: ${vector.norm}|limit: ${limit}), startPoint: $startPoint, currentPoint: $currentPoint")
			rotationAngle = vector.angle.toFloat()

			onAnimationStateUpdate(
				JoystickAnimationState.Dragging(
					x = currentPoint.x,
					y = currentPoint.y,
				)
			)
			onJoystickStateUpdate(
				JoystickState(
					angle = rotationAngle,
					strength = min(vector.norm, size.width.toFloat()),
					userInput = true,
				)
			)
		}
	}
	.offset {
		IntOffset(
			joystickAnimationState.x.toInt(),
			joystickAnimationState.y.toInt()
		)
	}


data class JoystickState(
	val angle: Float = 0F,
	val strength: Float = 0f,
	val userInput: Boolean = false,
)

private sealed class JoystickAnimationState(
	open val x: Float,
	open val y: Float,
	open val duration: Int,
) {
	object Idle : JoystickAnimationState(x = 0f, y = 0f, duration = 0)
	data class Dragging(
		override val x: Float,
		override val y: Float,
	) : JoystickAnimationState(x, y, duration = 0)

	data class Reset(override val duration: Int) : JoystickAnimationState(x = 0f, y = 0f, duration = duration)

}

private val Offset.angle: Double
	get() = atan(x.toDouble() / x.toDouble())

private val Offset.norm: Float
	get() = sqrt(x.pow(2) + y.pow(2))

private fun Offset.scaleOn(factor: Float): Offset = Offset(x * factor, y * factor)

private fun Offset.reduceNormIfBiggerThan(limit: Float): Offset {
	if (norm > limit) {
		val factor = limit.toDouble() / norm.toDouble()
		return scaleOn(factor.toFloat())
	}
	return this.copy()
}

@Composable
@Preview
private fun JoystickPreview() {
	MaterialTheme {
		Joystick(
			modifier = Modifier
				.width(200.dp)
				.height(200.dp),
			onStateUpdate = { }
		)
	}
}