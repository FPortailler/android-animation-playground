package me.portailler.florian.testanimation.ui.designs.state

import me.portailler.florian.testanimation.ui.designs.quarter.QuarterScreen

object DesignNavProvider {

	val states by lazy {
		listOf(
			DesignNavState(
				title = "Quarter",
				description = "A quarter of a circle",
				screen = QuarterScreen()
			)
		)
	}
}