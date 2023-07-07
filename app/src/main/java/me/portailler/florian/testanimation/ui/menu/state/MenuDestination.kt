package me.portailler.florian.testanimation.ui.menu.state

import android.app.Activity

data class MenuDestination(
	val title: String,
	val description: String,
	val activityClass: Class<Activity>,
)