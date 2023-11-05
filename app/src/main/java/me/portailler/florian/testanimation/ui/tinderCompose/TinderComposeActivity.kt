package me.portailler.florian.testanimation.ui.tinderCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import me.portailler.florian.testanimation.ui.tinderCompose.screen.TinderScreen

class TinderComposeActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				TinderScreen()
			}
		}
	}
}
