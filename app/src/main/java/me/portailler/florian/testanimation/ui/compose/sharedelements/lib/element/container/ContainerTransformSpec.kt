package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container

import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.graphics.Color
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.FitMode
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ProgressThresholds
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ProgressThresholdsGroup
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.ArcMotion
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.ArcMotionFactory
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.LinearMotionFactory
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.PathMotion
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.PathMotionFactory
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.TransitionDirection
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.FadeMode
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.SharedElementsTransitionSpec


class ContainerTransformSpec(
	pathMotionFactory: PathMotionFactory = LinearMotionFactory,
	/**
	 * Frames to wait for before starting transition. Useful when the frame skip caused by
	 * rendering the new screen makes the animation not smooth.
	 */
	waitForFrames: Int = 1,
	durationMillis: Int = AnimationConstants.DefaultDurationMillis,
	delayMillis: Int = 0,
	easing: Easing = FastOutSlowInEasing,
	direction: TransitionDirection = TransitionDirection.Auto,
	fadeMode: FadeMode = FadeMode.In,
	val fitMode: FitMode = FitMode.Auto,
	val startContainerColor: Color = Color.Transparent,
	val endContainerColor: Color = Color.Transparent,
	fadeProgressThresholds: ProgressThresholds? = null,
	scaleProgressThresholds: ProgressThresholds? = null,
	val scaleMaskProgressThresholds: ProgressThresholds? = null,
	val shapeMaskProgressThresholds: ProgressThresholds? = null
) : SharedElementsTransitionSpec(
	pathMotionFactory,
	waitForFrames,
	durationMillis,
	delayMillis,
	easing,
	direction,
	fadeMode,
	fadeProgressThresholds,
	scaleProgressThresholds
) {
	internal fun progressThresholdsGroupFor(
		direction: TransitionDirection,
		pathMotion: PathMotion
	): ProgressThresholdsGroup {
		val default = if (pathMotion is ArcMotion) {
			if (direction == TransitionDirection.Enter)
				ProgressThresholdsGroup.DefaultEnterArc else ProgressThresholdsGroup.DefaultReturnArc
		} else {
			if (direction == TransitionDirection.Enter)
				ProgressThresholdsGroup.DefaultEnter else ProgressThresholdsGroup.DefaultReturn
		}
		return ProgressThresholdsGroup(
			fadeProgressThresholds ?: default.fade,
			scaleProgressThresholds ?: default.scale,
			scaleMaskProgressThresholds ?: default.scaleMask,
			shapeMaskProgressThresholds ?: default.shapeMask
		)
	}

	companion object {
		val Default by lazy { ContainerTransformSpec() }
	}
}

public fun fadeIn(durationMillis: Int = AnimationConstants.DefaultDurationMillis) = ContainerTransformSpec(
	durationMillis = durationMillis,
	fadeMode = FadeMode.In,
	pathMotionFactory = ArcMotionFactory,
)
