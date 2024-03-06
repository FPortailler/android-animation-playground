package me.portailler.florian.testanimation.ui.menu.state

import me.portailler.florian.testanimation.ui.compose.changeicon.ChangeIconActivity
import me.portailler.florian.testanimation.ui.compose.designs.DesignHomeActivity
import me.portailler.florian.testanimation.ui.compose.joystick.JoystickActivity
import me.portailler.florian.testanimation.ui.compose.lottie.LottieComposeActivity
import me.portailler.florian.testanimation.ui.compose.shortcuts.ShortcutActivity
import me.portailler.florian.testanimation.ui.compose.tabbar.TabBarComposeActivity
import me.portailler.florian.testanimation.ui.compose.tinderCompose.TinderComposeActivity
import me.portailler.florian.testanimation.ui.compose.video.VideoActivity
import me.portailler.florian.testanimation.ui.xml.lottie.LottieActivity
import me.portailler.florian.testanimation.ui.xml.modale.ModalActivity
import me.portailler.florian.testanimation.ui.xml.program.OptimizedProgramActivity
import me.portailler.florian.testanimation.ui.xml.program.ProgramActivity
import me.portailler.florian.testanimation.ui.xml.sharedelement.SharedElementActivity
import me.portailler.florian.testanimation.ui.xml.snackbar.SnackbarActivity
import me.portailler.florian.testanimation.ui.xml.tinder.TinderActivity

object MenuDestinationProvider {
	val items by lazy {
		listOf(
			MenuDestination(
				title = "Designs",
				description = "A sub page of random designs",
				activityClass = DesignHomeActivity::class.java
			),
			MenuDestination(
				title = "Tinder",
				description = "Swipe cards",
				activityClass = TinderActivity::class.java
			),
			MenuDestination(
				title = "Tinder Compose",
				description = "Swipe cards now in compose",
				activityClass = TinderComposeActivity::class.java
			),
			MenuDestination(
				title = "Shared Element",
				description = "Image transition from a list to a detail",
				activityClass = SharedElementActivity::class.java
			),
			MenuDestination(
				title = "Joystick",
				description = "Just a joystick composable",
				activityClass = JoystickActivity::class.java
			),
			MenuDestination(
				title = "Snackbar",
				description = "A snackbar with a custom animation",
				activityClass = SnackbarActivity::class.java
			),
			MenuDestination(
				title = "Modal",
				description = "An attempt to make iOS modal look and feel",
				activityClass = ModalActivity::class.java
			),
			MenuDestination(
				title = "Lottie",
				description = "A lottie animation",
				activityClass = LottieActivity::class.java
			),
			MenuDestination(
				title = "Lottie Compose",
				description = "A lottie animation in compose",
				activityClass = LottieComposeActivity::class.java
			),
			MenuDestination(
				title = "Shortcut",
				description = "A shortcut to open the app",
				activityClass = ShortcutActivity::class.java
			),
			MenuDestination(
				title = "Program",
				description = "A program schedule",
				activityClass = ProgramActivity::class.java
			),
			MenuDestination(
				title = "Program Optimized",
				description = "A program schedule with a RecyclerView",
				activityClass = OptimizedProgramActivity::class.java
			),
			MenuDestination(
				title = "Tab bar",
				description = "A highly customizable tab bar in compose",
				activityClass = TabBarComposeActivity::class.java
			),
			MenuDestination(
				title = "Change Icon",
				description = "Change the app icon",
				activityClass = ChangeIconActivity::class.java
			),
			MenuDestination(
				title = "Video player compose",
				description = "A video player in compose",
				activityClass = VideoActivity::class.java
			)
		)
	}
}