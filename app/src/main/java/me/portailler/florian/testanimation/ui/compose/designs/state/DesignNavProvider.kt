package me.portailler.florian.testanimation.ui.compose.designs.state

import me.portailler.florian.testanimation.ui.compose.designs.customBrush.SquircleScreen
import me.portailler.florian.testanimation.ui.compose.designs.quarter.QuarterScreen

object DesignNavProvider {

	val states by lazy {
		listOf(
			DesignNavState(
				title = "Quarter",
				description = "A quarter of a circle",
				screen = QuarterScreen()
			),
			DesignNavState(
				title = "Squicle",
				description = "An example of a squircle",
				screen = SquircleScreen()
			),
		)
	}
}