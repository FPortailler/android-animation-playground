package me.portailler.florian.testanimation.ui.joystick.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.portailler.florian.testanimation.ui.joystick.joystick.Joystick

@Composable
fun JoystickScreen(
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Bottom,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Joystick(
			modifier = Modifier
				.width(200.dp)
				.height(200.dp),
			onStateUpdate = { joystickState ->
				Log.d("JoystickScreen", "JoystickState: $joystickState")
			}
		)
	}
}
