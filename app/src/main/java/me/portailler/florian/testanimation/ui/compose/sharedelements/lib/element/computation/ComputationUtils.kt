package computation

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.FitMode

internal object ComputationUtils {

	fun calculateFitMode(entering: Boolean, start: Rect, end: Rect): FitMode {
		val startWidth = start.width
		val startHeight = start.height
		val endWidth = end.width
		val endHeight = end.height

		val endHeightFitToWidth = endHeight * startWidth / endWidth
		val startHeightFitToWidth = startHeight * endWidth / startWidth
		val fitWidth = if (entering)
			endHeightFitToWidth >= startHeight else startHeightFitToWidth >= endHeight
		return if (fitWidth) FitMode.Width else FitMode.Height
	}

	private fun lerp(start: CornerSize?, end: CornerSize?, fraction: Float): CornerSize? {
		if (start == null && end == null) return null
		return object : CornerSize {
			override fun toPx(shapeSize: Size, density: Density): Float =
				androidx.compose.ui.util.lerp(
					start?.toPx(shapeSize, density) ?: 0f,
					end?.toPx(shapeSize, density) ?: 0f,
					fraction
				)
		}
	}

	fun lerp(start: Shape, end: Shape, fraction: Float): Shape {
		if ((start == RectangleShape && end == RectangleShape) ||
			(start != RectangleShape && start !is CornerBasedShape) ||
			(end != RectangleShape && end !is CornerBasedShape)
		) return start
		val topStart = lerp(
			(start as? CornerBasedShape)?.topStart,
			(end as? CornerBasedShape)?.topStart,
			fraction
		) ?: ZeroCornerSize
		val topEnd = lerp(
			(start as? CornerBasedShape)?.topEnd,
			(end as? CornerBasedShape)?.topEnd,
			fraction
		) ?: ZeroCornerSize
		val bottomEnd = lerp(
			(start as? CornerBasedShape)?.bottomEnd,
			(end as? CornerBasedShape)?.bottomEnd,
			fraction
		) ?: ZeroCornerSize
		val bottomStart = lerp(
			(start as? CornerBasedShape)?.bottomStart,
			(end as? CornerBasedShape)?.bottomStart,
			fraction
		) ?: ZeroCornerSize
		return when {
			start is RoundedCornerShape || (start == RectangleShape && end is RoundedCornerShape) ->
				RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)

			start is CutCornerShape || (start == RectangleShape && end is CutCornerShape) ->
				CutCornerShape(topStart, topEnd, bottomEnd, bottomStart)

			else -> start
		}
	}
}