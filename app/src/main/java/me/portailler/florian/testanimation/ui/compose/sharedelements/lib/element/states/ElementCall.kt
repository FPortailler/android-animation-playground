package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.ui.Modifier

internal class ElementCall(
	val screenKey: Any,
	val containerModifier: Modifier,
	val relaxMaxSize: Boolean,
	val contentModifier: Modifier,
	val compositionLocalContext: CompositionLocalContext,
	val content: @Composable () -> Unit
)