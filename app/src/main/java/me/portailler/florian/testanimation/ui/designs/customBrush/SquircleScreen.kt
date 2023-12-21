package me.portailler.florian.testanimation.ui.designs.customBrush

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

class SquircleScreen : Screen {

	companion object {
		private const val DEFAULT_RADIUS = 50f
	}

	@Composable
	override fun Content() {
		var radiusTL by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var radiusTR by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var radiusBR by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var radiusBL by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var smoothnessTL by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var smoothnessTR by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var smoothnessBR by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		var smoothnessBL by remember {
			mutableStateOf(DEFAULT_RADIUS)
		}
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color(0xffFDF6EC))
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.verticalScroll(rememberScrollState())
					.padding(60.dp, 0.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Surface(
					modifier = Modifier
						.padding(25.dp)
						.height(200.dp)
						.width(200.dp),
					color = Color(0xFF34ebeb),
					shape = AbsoluteSmoothCornerShape(
						Dp(radiusTL), smoothnessTL.toInt(),
						Dp(radiusTR), smoothnessTR.toInt(),
						Dp(radiusBR), smoothnessBR.toInt(),
						Dp(radiusBL), smoothnessBL.toInt()
					)
				) {}
				Text(text = "Radius top-left: ${radiusTL.toInt()}")
				Slider(
					value = radiusTL,
					onValueChange = { radiusTL = it },
					valueRange = 0f..100f
				)
				Text(text = "Smoothness top-left: ${smoothnessTL.toInt()}")
				Slider(
					value = smoothnessTL,
					onValueChange = { smoothnessTL = it },
					valueRange = 0f..100f
				)

				Text(text = "Radius top-right: ${radiusTR.toInt()}")
				Slider(
					value = radiusTR,
					onValueChange = { radiusTR = it },
					valueRange = 0f..100f
				)
				Text(text = "Smoothness top-right: ${smoothnessTR.toInt()}")
				Slider(
					value = smoothnessTR,
					onValueChange = { smoothnessTR = it },
					valueRange = 0f..100f
				)

				Text(text = "Radius bottom-right: ${radiusBR.toInt()}")
				Slider(
					value = radiusBR,
					onValueChange = { radiusBR = it },
					valueRange = 0f..100f
				)
				Text(text = "Smoothness bottom-right: ${smoothnessBR.toInt()}")
				Slider(
					value = smoothnessBR,
					onValueChange = { smoothnessBR = it },
					valueRange = 0f..100f
				)

				Text(text = "Radius bottom-left: ${radiusBL.toInt()}")
				Slider(
					value = radiusBL,
					onValueChange = { radiusBL = it },
					valueRange = 0f..100f
				)
				Text(text = "Smoothness bottom-left: ${smoothnessBL.toInt()}")
				Slider(
					value = smoothnessBL,
					onValueChange = { smoothnessBL = it },
					valueRange = 0f..100f
				)
			}
		}
	}
}