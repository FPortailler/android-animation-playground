package me.portailler.florian.testanimation.ui.program.logic

import kotlin.math.max
import kotlin.random.Random

object ProgramManager {

	const val HALF_HOUR = 1000 * 60 * 30L
	private const val PROGRAM_COUNT = 100
	private const val PROGRAM_MAX_SPAN = 4
	const val CHANNEL_COUNT = 10


	val programs: List<ProgramEntity> by lazy {
		val now = System.currentTimeMillis()
		val random = Random(seed = 1)
		val size = PROGRAM_COUNT
		val channelCount = CHANNEL_COUNT
		val lastProgramsOfChannels = mutableMapOf<Int, ProgramEntity>()
		List(size = size) {
			val channel = random.nextInt(channelCount)
			var startTime = now + (size / 2 - it) * HALF_HOUR
			val previousProgram = lastProgramsOfChannels[channel]
			if (previousProgram?.endTime != null && previousProgram.endTime > startTime) {
				startTime = previousProgram.endTime
			}
			val endTime = startTime + random.nextInt(1, PROGRAM_MAX_SPAN) * HALF_HOUR
			ProgramEntity(
				id = it.toString(),
				title = "Program $it",
				description = "Description $it",
				startTime = startTime,
				endTime = endTime,
				channel = channel
			)
				.also { program -> lastProgramsOfChannels[channel] = program }
		}
	}

	fun ProgramEntity.getSpan(): Long {
		return max(1L, (endTime - startTime) / HALF_HOUR - 1)
	}
}