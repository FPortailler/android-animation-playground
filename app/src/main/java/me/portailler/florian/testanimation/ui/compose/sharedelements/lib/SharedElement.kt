package me.portailler.florian.testanimation.ui.compose.sharedelements.lib

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.Placeholder
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container.ContainerTransformSpec
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container.MaterialContainer
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ElementInfo
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.BaseSharedElement

@Composable
public fun SharedElement(
	key: Any,
	screenKey: Any,
	isFullscreen: Boolean = false,
	shape: Shape = RectangleShape,
	color: Color = MaterialTheme.colorScheme.surface,
	contentColor: Color = contentColorFor(color),
	border: BorderStroke? = null,
	elevation: Dp = 0.dp,
	transitionSpec: ContainerTransformSpec = ContainerTransformSpec.Default,
	onFractionChanged: ((Float) -> Unit)? = null,
	placeholder: @Composable (() -> Unit)? = null,
	content: @Composable () -> Unit
) {
	val elementInfo = ElementInfo(
		key, screenKey, shape, color, contentColor,
		border, elevation, transitionSpec, onFractionChanged
	)
	val realPlaceholder = placeholder ?: content
	BaseSharedElement(
		elementInfo,
		isFullscreen,
		realPlaceholder,
		{ Placeholder(it) },
		{
			MaterialContainer(
				modifier = it,
				shape = shape,
				backgroundColor = color,
				contentColor = contentColor,
				border = border,
				elevation = elevation,
				content = content
			)
		}
	)
}
