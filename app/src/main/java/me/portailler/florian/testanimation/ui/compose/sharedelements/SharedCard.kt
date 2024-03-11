package me.portailler.florian.testanimation.ui.compose.sharedelements

import android.support.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.FadeMode
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.MaterialArcMotionFactory
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.MaterialContainerTransformSpec
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.SharedMaterialContainer

@Composable
fun SharedCard(
	modifier: Modifier = Modifier,
	@DrawableRes imageUri: Int,
	title: String,
	subtitle: String,
	clickable: Boolean = true,
	onClick: () -> Unit = {},
) {
	var aspectRatio by remember { mutableFloatStateOf(1f) }
	Row(
		modifier = modifier
			.background(
				color = MaterialTheme.colorScheme.secondaryContainer,
				shape = MaterialTheme.shapes.extraLarge,
			)
			.clip(MaterialTheme.shapes.extraLarge),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(16.dp),
	) {
		SharedMaterialContainer(
			key = title,
			screenKey = "list",
			transitionSpec = MaterialFadeInTransitionSpec,
			onFractionChanged = {
				aspectRatio = 1f * (1 - it) + 16f.div(9f) * it
			}
		) {
			AsyncImage(
				model = imageUri,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.width(100.dp)
					.aspectRatio(aspectRatio)
					.wrapContentHeight(align = Alignment.Top),
			)
		}
		Column(
			modifier = Modifier.weight(1f),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start,
		) {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight(align = Alignment.CenterVertically),
				text = title,
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				maxLines = 1,
			)
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight(align = Alignment.CenterVertically),
				text = subtitle,
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				maxLines = 2,
			)
		}
		Icon(
			imageVector = Icons.Filled.Info,
			contentDescription = null,
			modifier = Modifier
				.size(48.dp)
				.background(
					color = MaterialTheme.colorScheme.primary,
					shape = CircleShape,
				)
				.clip(CircleShape)
				.clickable(enabled = clickable, onClick = onClick)
				.padding(4.dp)
				.wrapContentHeight(align = Alignment.CenterVertically),
			tint = MaterialTheme.colorScheme.onPrimary,
		)
		Spacer(modifier = Modifier.size(8.dp))
	}
}

@PreviewLightDark
@Composable
@Suppress("UnusedPrivateMember")
private fun SharedItemPreview() {
	MaterialTheme {
		SharedCard(
			imageUri = R.drawable.mock_image,
			title = "Title",
			subtitle = "Subtitle",
		)
	}
}

private val MaterialFadeInTransitionSpec = MaterialContainerTransformSpec(
	pathMotionFactory = MaterialArcMotionFactory,
	durationMillis = 1_000,
	fadeMode = FadeMode.In
)

