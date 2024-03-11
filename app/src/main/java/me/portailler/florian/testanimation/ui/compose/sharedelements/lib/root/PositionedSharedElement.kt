package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.ui.geometry.Rect


internal class PositionedSharedElement(
	val info: SharedElementInfo,
	val compositionLocalContext: CompositionLocalContext,
	val placeholder: @Composable () -> Unit,
	val overlay: @Composable (SharedElementsTransitionState) -> Unit,
	val bounds: Rect?
)