package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states

import androidx.compose.runtime.Immutable


@Immutable
internal class ProgressThresholdsGroup(
	val fade: ProgressThresholds,
	val scale: ProgressThresholds,
	val scaleMask: ProgressThresholds,
	val shapeMask: ProgressThresholds
) {
	companion object {

		// Default animation thresholds. Will be used by default when the default linear PathMotion is
// being used or when no other progress thresholds are appropriate (e.g., the arc thresholds for
// an arc path).
		val DefaultEnter = ProgressThresholdsGroup(
			fade = ProgressThresholds(0f, 0.25f),
			scale = ProgressThresholds(0f, 1f),
			scaleMask = ProgressThresholds(0f, 1f),
			shapeMask = ProgressThresholds(0f, 0.75f)
		)
		internal val DefaultReturn = ProgressThresholdsGroup(
			fade = ProgressThresholds(0.60f, 0.90f),
			scale = ProgressThresholds(0f, 1f),
			scaleMask = ProgressThresholds(0f, 0.90f),
			shapeMask = ProgressThresholds(0.30f, 0.90f)
		)

		// Default animation thresholds for an arc path. Will be used by default when the PathMotion is
// set to MaterialArcMotion.
		internal val DefaultEnterArc = ProgressThresholdsGroup(
			fade = ProgressThresholds(0.10f, 0.40f),
			scale = ProgressThresholds(0.10f, 1f),
			scaleMask = ProgressThresholds(0.10f, 1f),
			shapeMask = ProgressThresholds(0.10f, 0.90f)
		)
		internal val DefaultReturnArc = ProgressThresholdsGroup(
			fade = ProgressThresholds(0.60f, 0.90f),
			scale = ProgressThresholds(0f, 0.90f),
			scaleMask = ProgressThresholds(0f, 0.90f),
			shapeMask = ProgressThresholds(0.20f, 0.90f)
		)
	}
}