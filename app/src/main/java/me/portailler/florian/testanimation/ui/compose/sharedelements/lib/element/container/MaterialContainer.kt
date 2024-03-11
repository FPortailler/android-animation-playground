package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp


@Composable
internal fun MaterialContainer(
	modifier: Modifier,
	shape: Shape,
	backgroundColor: Color,
	contentColor: Color,
	border: BorderStroke?,
	elevation: Dp,
	content: @Composable () -> Unit
) {
	CompositionLocalProvider(
		LocalContentColor provides contentColor,
	) {
		Box(
			modifier = modifier
				.shadow(elevation, shape, clip = false)
				.then(if (border != null) Modifier.border(border, shape) else Modifier)
				.background(
					color = backgroundColor,
					shape = shape
				)
				.clip(shape),
			propagateMinConstraints = true
		) {
			content()
		}
	}
}

