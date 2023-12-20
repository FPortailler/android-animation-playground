package me.portailler.florian.testanimation.ui.designs.state

import cafe.adriel.voyager.core.screen.Screen

data class DesignNavState(
	val title: String,
	val description: String,
	val screen: Screen
)