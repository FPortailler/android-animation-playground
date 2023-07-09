package me.portailler.florian.testanimation.ui.tinder.card

data class TinderCardEntity(
	val id: String,
	val title: String,
	val description: String,
	val url: String = url(id)
) {

	companion object {
		private fun url(id: String) = "https://picsum.photos/seed/$id/158/229"

	}
}