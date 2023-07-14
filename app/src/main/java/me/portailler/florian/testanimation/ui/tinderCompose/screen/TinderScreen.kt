package me.portailler.florian.testanimation.ui.tinderCompose.screen

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.portailler.florian.testanimation.ui.tinder.TinderViewModel
import me.portailler.florian.testanimation.ui.tinderCompose.box.DragAndSwipeBox
import me.portailler.florian.testanimation.ui.tinderCompose.card.TinderCard

@Composable
fun TinderScreen(
	viewModel: TinderViewModel = TinderViewModel()
) {
	val entities by viewModel.entities.collectAsStateWithLifecycle()

	LaunchedEffect(true) {
		viewModel.loadEntities()
	}
	DragAndSwipeBox(
		modifier = Modifier.padding(48.dp),
		itemCount = entities.size,
		build = { modifier, index ->
			TinderCard(
				modifier = modifier,
				entity = entities[index]
			)
		}, onSwipe = { direction: Int, index: Int ->
			Log.d("TinderScreen", "onSwipe: $direction, $index")
		}, onDrag = { index: Int, progress: Float ->
			Log.d("TinderScreen", "onDrag: $index, $progress")
		})
}