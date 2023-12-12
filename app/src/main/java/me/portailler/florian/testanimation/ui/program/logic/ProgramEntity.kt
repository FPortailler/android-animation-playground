package me.portailler.florian.testanimation.ui.program.logic

data class ProgramEntity(
	val id: String,
	val title: String,
	val description: String,
	val startTime: Long,
	val endTime: Long,
	val channel: Channel,
) {

	enum class Channel {
		TF1,
		FRANCE_2,
		FRANCE_3,
		FRANCE_5,
		FRANCE_O,
	}
}