package me.portailler.florian.testanimation.ui.tinder.card

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class TinderCardEntity(
	val id: String,
	val title: String,
	val description: String,
	val url: String = url(id)
) {

	companion object {
		private fun url(id: String) = "https://picsum.photos/seed/$id/1024/2048"

		val mocks: List<TinderCardEntity> by lazy {
			(1..16).map { id ->
				TinderCardEntity(
					id = id.toString(),
					title = "Title $id",
					description = "Description $id"
				)
			}
		}

	}
}

class TinderCardEntityPreviewProvider : PreviewParameterProvider<TinderCardEntity> {
	override val values: Sequence<TinderCardEntity>
		get() = TinderCardEntity.mocks.asSequence()
}
