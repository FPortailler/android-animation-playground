package me.portailler.florian.testanimation.ui.compose.sharedelements

import android.support.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class SharedItem(
	@DrawableRes val imageUri: Int,
	val title: String,
	val subtitle: String,
)