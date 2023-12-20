package me.portailler.florian.testanimation.ui.designs.quarter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen

class QuarterScreen : Screen {
	@Composable
	override fun Content() {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(1f)
				.background(
					brush = Brush.radialGradient(
						radius = 1000f,
						colorStops = arrayOf(
							0f to Color(0xFF34ebeb),
							1f to Color.White
						)
					)
				)
		) {
			Row {
				Box(
					modifier = Modifier
						.weight(1f)
						.background(
							brush = Brush.radialGradient(
								radius = 1000f,
								colorStops = arrayOf(
									0f to Color(0xFF34ebeb),
									1f to Color.White
								),
							)
						)
				)
				Box(
					modifier = Modifier
						.weight(1f)
						.background(
							brush = Brush.radialGradient(
								radius = 1000f,
								colorStops = arrayOf(
									0f to Color(0xFF34ebeb),
									1f to Color.White
								)
							)
						)
				)
			}
			Row {
				Box(
					modifier = Modifier
						.weight(1f)
						.background(
							brush = Brush.radialGradient(
								radius = 1000f,
								colorStops = arrayOf(
									0f to Color(0xFF34ebeb),
									1f to Color.White
								),
							)
						)
				)
				Box(
					modifier = Modifier
						.weight(1f)
						.background(
							brush = Brush.radialGradient(
								radius = 1000f,
								colorStops = arrayOf(
									0f to Color(0xFF34ebeb),
									1f to Color.White
								)
							)
						)
				)
			}
		}
	}
}