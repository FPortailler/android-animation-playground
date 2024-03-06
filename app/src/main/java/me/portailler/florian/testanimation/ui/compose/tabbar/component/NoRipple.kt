package me.portailler.florian.testanimation.ui.compose.tabbar.component

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun NoRipple(
	content: @Composable () -> Unit,
) {
	CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme, content = content)
}

private object NoRippleTheme : RippleTheme {
	@Composable
	override fun defaultColor(): Color {
		return Color.Transparent
	}

	@Composable
	override fun rippleAlpha(): RippleAlpha {
		return RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
	}
}