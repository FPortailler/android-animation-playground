package me.portailler.florian.testanimation.ui.tinderCompose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.portailler.florian.testanimation.ui.tinder.TinderViewModel
import me.portailler.florian.testanimation.ui.tinderCompose.card.TinderCard

@Composable
fun TinderScreen(
	viewModel: TinderViewModel = TinderViewModel()
) {
	val entities by viewModel.entities.collectAsStateWithLifecycle()

	LaunchedEffect(true) {
		viewModel.loadEntities()
	}
	Box(
		modifier = Modifier.fillMaxSize()
	) {
		entities.firstOrNull()?.let { entity ->
			TinderCard(entity)
		}
	}
}