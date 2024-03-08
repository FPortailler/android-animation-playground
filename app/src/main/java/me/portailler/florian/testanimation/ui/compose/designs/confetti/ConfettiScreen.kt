package me.portailler.florian.testanimation.ui.compose.designs.confetti

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import cafe.adriel.voyager.core.screen.Screen
import me.portailler.florian.testanimation.R
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.models.Shape

class ConfettiScreen : Screen {

	@Composable
	override fun Content() {
		ConfettiScreen(
			modifier = Modifier.fillMaxSize(),
		)
	}
}

@Composable
fun ConfettiScreen(
	modifier: Modifier = Modifier,
) {
	var state: State by remember { mutableStateOf(State.Idle) }
	val drawable = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_confetti)
	when (val newState = state) {
		State.Idle -> {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.fillMaxHeight(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Button(onClick = { state = State.Started(Presets.festive(ImageUtils.loadDrawable(drawable!!))) }) {
					Text(
						text = "Festive",
					)
				}
				Button(onClick = { state = State.Started(Presets.explode()) }) {
					Text(
						text = "Explode",
					)
				}
				Button(onClick = { state = State.Started(Presets.parade()) }) {
					Text(
						text = "Parade",
					)
				}
				Button(onClick = { state = State.Started(Presets.rain()) }) {
					Text(
						text = "Rain",
					)
				}
			}
		}

		is State.Started -> {
			KonfettiView(
				modifier = modifier,
				parties = newState.party,
				updateListener = object : OnParticleSystemUpdateListener {
					override fun onParticleSystemEnded(
						system: PartySystem,
						activeSystems: Int,
					) {
						if (activeSystems == 0) state = State.Idle
					}
				},
			)
		}
	}

}

private sealed class State {
	class Started(val party: List<Party>) : State()

	object Idle : State()
}

private object ImageUtils {
	@JvmStatic
	fun loadDrawable(
		drawable: Drawable,
		tint: Boolean = true,
		applyAlpha: Boolean = true,
	): Shape.DrawableShape {
		val width = drawable.intrinsicWidth
		val height = drawable.intrinsicHeight
		return Shape.DrawableShape(drawable, tint, applyAlpha)
	}
}


@PreviewLightDark
@PreviewDynamicColors
@Composable
@Suppress("UnusedPrivateMember")
private fun ConfettiScreenPreview() {
	MaterialTheme {
		ConfettiScreen()
	}
}