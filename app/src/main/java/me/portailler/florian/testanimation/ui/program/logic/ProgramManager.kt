package me.portailler.florian.testanimation.ui.program.logic

import kotlin.random.Random

object ProgramManager {

	const val HALF_HOUR = 1000 * 60 * 30L


	val programs: List<ProgramEntity> by lazy {
		val now = System.currentTimeMillis()
		val random = Random(seed = 0)
		val size = 100
		List(size = size) {
			val startTime = now + (size / 2 - it) * HALF_HOUR
			val endTime = startTime + (1 + random.nextInt() % 3) * HALF_HOUR
			ProgramEntity(
				id = it.toString(),
				title = "Program $it",
				description = "Description $it",
				startTime = startTime,
				endTime = endTime,
				channel = ProgramEntity.Channel.values().random()
			)
		}
	}
}