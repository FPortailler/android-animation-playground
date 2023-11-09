package me.portailler.florian.testanimation.ui.lottie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.ui.lottie.screens.LottieScreen


class LottieComposeActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				LottieScreen(rawRes = R.raw.spacemon)
			}
		}
	}

}