package me.portailler.florian.testanimation.ui.compose.sharedelements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.SharedElement
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container.ContainerTransformSpec
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.motion.ArcMotionFactory
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.LocalSharedElementsRootScope
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.select
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.FadeMode


@Composable
fun SharedElementDetailsScreen(
	items: List<SharedItem>,
	modifier: Modifier = Modifier,
) {
	val item = items.getOrNull(LocalSharedElementsRootScope.current!!.selectedIndex)
	val (fraction, setFraction) = remember { mutableFloatStateOf(1f) }
	if (item != null) Surface(
		modifier = modifier
			.alpha(fraction),
		color = MaterialTheme.colorScheme.background.copy(alpha = 0.32f * fraction),
	) {
		Column {
			SharedElement(
				key = item.title,
				screenKey = "details",
				isFullscreen = true,
				transitionSpec = MaterialFadeOutTransitionSpec,
				onFractionChanged = setFraction,
			) {
				val scope = LocalSharedElementsRootScope.current!!
				if (!scope.isRunningTransition) Box(
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(16f.div(9f)),
					contentAlignment = Alignment.TopEnd,
				) {
					AsyncImage(
						modifier = Modifier
							.fillMaxWidth()
							.aspectRatio(16f.div(9f)),
						model = item.imageUri,
						contentDescription = null,
						contentScale = ContentScale.Crop,
					)
					Icon(
						modifier = Modifier
							.size(48.dp)
							.background(
								color = MaterialTheme.colorScheme.surface,
								shape = CircleShape
							)
							.clip(CircleShape)
							.clickable(enabled = !scope.isRunningTransition, onClick = {
								scope.select(-1) { listOf(items[it].imageUri) }
							}),
						imageVector = Icons.Filled.Close,
						contentDescription = "Close"
					)
				}
			}
		}
	}
}


private val MaterialFadeOutTransitionSpec = ContainerTransformSpec(
	pathMotionFactory = ArcMotionFactory,
	durationMillis = 1_000,
	fadeMode = FadeMode.Out
)