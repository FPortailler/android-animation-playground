package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion

import androidx.compose.ui.geometry.Offset
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.computation.QuadraticBezier

class ArcMotion : KeyframeBasedMotion() {

    override fun getKeyframes(start: Offset, end: Offset): Pair<FloatArray, LongArray> =
        QuadraticBezier.approximate(
            start,
            if (start.y > end.y) Offset(end.x, start.y) else Offset(start.x, end.y),
            end,
            0.5f
        )

}

val ArcMotionFactory: PathMotionFactory = { ArcMotion() }
