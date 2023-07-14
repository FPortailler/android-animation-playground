package me.portailler.florian.testanimation.ui.tinderCompose.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import coil.compose.AsyncImage
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntity
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntityPreviewProvider

@Composable
fun TinderCard(
	entity: TinderCardEntity
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.clip(MaterialTheme.shapes.medium)
			.background(MaterialTheme.colorScheme.surface),
		contentAlignment = Alignment.BottomCenter
	) {
		AsyncImage(
			model = entity.url,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier.fillMaxSize()
		)
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Brush.verticalGradient(
					listOf(
						MaterialTheme.colorScheme.primary.copy(alpha = 0f),
						MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
						MaterialTheme.colorScheme.primary.copy(alpha = 1f),
					)
				)),
			contentAlignment = Alignment.BottomCenter
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(text = entity.title)
				Text(text = entity.description)
			}
		}

	}
}


@Preview
@Composable
private fun TinderCardPreview(
	@PreviewParameter(TinderCardEntityPreviewProvider::class) entity: TinderCardEntity
) {
	MaterialTheme {
		TinderCard(entity)
	}
}