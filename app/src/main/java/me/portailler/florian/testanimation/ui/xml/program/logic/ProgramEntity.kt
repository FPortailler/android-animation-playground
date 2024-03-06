package me.portailler.florian.testanimation.ui.xml.program.logic

data class ProgramEntity(
	val id: String,
	val title: String,
	val description: String,
	val startTime: Long,
	val endTime: Long,
	val channel: Int,
)