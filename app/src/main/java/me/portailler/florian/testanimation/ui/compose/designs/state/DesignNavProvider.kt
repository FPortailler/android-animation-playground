package me.portailler.florian.testanimation.ui.compose.designs.state

import me.portailler.florian.testanimation.ui.compose.designs.confetti.ConfettiScreen
import me.portailler.florian.testanimation.ui.compose.designs.customBrush.SquircleScreen
import me.portailler.florian.testanimation.ui.compose.designs.quarter.QuarterScreen
import me.portailler.florian.testanimation.ui.compose.sharedelements.SharedElementScreen

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
			DesignNavState(
				title = "Confetti",
				description = "A confetti animation",
				screen = ConfettiScreen()
			),
			DesignNavState(
				title = "Shared Elements",
				description = "A Shared elements example with a fork of a library",
				screen = SharedElementScreen()
			),
		)
	}
}