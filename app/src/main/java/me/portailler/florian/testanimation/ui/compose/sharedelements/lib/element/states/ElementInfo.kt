package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.SharedElementInfo
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.SharedElementsTransitionSpec

internal class ElementInfo(
	key: Any,
	screenKey: Any,
	val shape: Shape,
	val color: Color,
	val contentColor: Color,
	val border: BorderStroke?,
	val elevation: Dp,
	spec: SharedElementsTransitionSpec,
	onFractionChanged: ((Float) -> Unit)?,
) : SharedElementInfo(key, screenKey, spec, onFractionChanged)
