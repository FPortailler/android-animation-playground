package me.portailler.florian.testanimation.ui.compose.lottie.screens

import android.support.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieScreen(
	modifier: Modifier = Modifier,
	@RawRes rawRes: Int,
) {
	val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(rawRes))
	LottieAnimation(
		modifier = modifier
			.fillMaxSize(),
		composition = composition,
	)

}