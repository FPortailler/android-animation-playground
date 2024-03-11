package me.portailler.florian.testanimation.ui.compose.sharedelements

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.thedeanda.lorem.LoremIpsum
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.LocalSharedElementsRootScope
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.SharedElementsRoot
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.SharedElementsRootScope
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.select

class SharedElementScreen : Screen {

	private val items: List<SharedItem> by lazy {
		(0..100).map {
			SharedItem(
				imageUri = R.drawable.mock_image,
				title = LoremIpsum.getInstance().getTitle(1, 7),
				subtitle = LoremIpsum.getInstance().getWords(10, 25),
			)
		}
	}

	@Composable
	override fun Content() {
		SharedElementsRoot {
			val scope = LocalSharedElementsRootScope.current!!
			Crossfade(
				targetState = scope.selectedIndex,
				animationSpec = tween(1_000), label = ""
			) { index ->
				when (index) {
					-1 -> SharedElementScreen(
						modifier = Modifier.fillMaxSize(),
						scope = scope,
						items = items,
						onItemSelected = { item ->
							scope.select(items.indexOf(item)) { index -> listOf(items[index].imageUri) }
						}
					)

					else -> SharedElementDetailsScreen(
						modifier = Modifier.fillMaxSize(),
						items = items
					)
				}
			}
		}
	}
}

@Composable
private fun SharedElementScreen(
	modifier: Modifier = Modifier,
	scope: SharedElementsRootScope,
	items: List<SharedItem>,
	onItemSelected: (SharedItem) -> Unit = {},
) {
	LazyColumn(
		modifier = modifier.padding(top = 16.dp, bottom = 16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		items(items) {
			SharedCard(
				modifier = Modifier
					.padding(horizontal = 16.dp)
					.fillMaxWidth(),
				imageUri = it.imageUri,
				title = it.title,
				subtitle = it.subtitle,
				clickable = !scope.isRunningTransition,
				onClick = { onItemSelected(it) }
			)
		}
	}
}


@Composable
@Preview
private fun SharedElementScreenPreview() {
	MaterialTheme {
		SharedElementScreen(
			modifier = Modifier.fillMaxSize(),
			scope = LocalSharedElementsRootScope.current!!,
			items = (0..10).map {
				SharedItem(
					imageUri = R.drawable.mock_image,
					title = LoremIpsum.getInstance().getTitle(5, 20),
					subtitle = LoremIpsum.getInstance().getWords(10, 25),
				)
			}
		)
	}
}