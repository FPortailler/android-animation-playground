package me.portailler.florian.testanimation.ui.compose.tabbar.state

import androidx.compose.ui.graphics.vector.ImageVector

data class TabBarItem(
	val icon: ImageVector,
	val selectedIcon: ImageVector = icon,
	val label: String?,
)