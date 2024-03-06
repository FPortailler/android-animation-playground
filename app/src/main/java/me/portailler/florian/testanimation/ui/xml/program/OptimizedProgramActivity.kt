package me.portailler.florian.testanimation.ui.xml.program

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import me.portailler.florian.testanimation.databinding.OptimizedProgramActivityBinding
import me.portailler.florian.testanimation.ui.xml.program.component.TimeTableState
import me.portailler.florian.testanimation.ui.xml.program.logic.ProgramManager
import me.portailler.florian.testanimation.ui.xml.program.logic.ProgramManager.getSpan

class OptimizedProgramActivity : AppCompatActivity() {

	private lateinit var binding: OptimizedProgramActivityBinding
	private lateinit var optimizedProgramAdapter: OptimizedProgramAdapter
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = OptimizedProgramActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		loadProgram()
	}

	private fun loadProgram() {
		val programs = ProgramManager.programs
		binding.programChannelList.removeAllViews()
		val startTime = programs.minOf { it.startTime }
		val endTime = programs.maxOf { it.endTime }
		val spanCount = (endTime - startTime) / ProgramManager.HALF_HOUR + 1
		optimizedProgramAdapter = OptimizedProgramAdapter(spanCount.toInt() + 1)
		(binding.programList.layoutManager as? GridLayoutManager)?.spanCount = spanCount.toInt()
		(binding.programList.layoutManager as? GridLayoutManager)?.spanSizeLookup = optimizedProgramAdapter.buildSpanSizeLookup()
		binding.programList.adapter = optimizedProgramAdapter
		val programAsTable: List<List<TimeTableState>> = programs
			.sortedBy { it.channel }
			.groupBy { it.channel }
			.map { (channel, programs) ->
				val programStates = programs.sortedBy { program ->
					program.startTime
				}.map { program ->
					OptimizedProgramAdapter.ProgramState.Program(
						program,
						startAt = ((program.startTime - startTime) / ProgramManager.HALF_HOUR).toInt() + 1,
						span = program.getSpan().toInt(),
						color = Color.HSVToColor(floatArrayOf((channel * 360f / ProgramManager.CHANNEL_COUNT), 1f, 0.5f))
					)
				}
				listOf<OptimizedProgramAdapter.ProgramState>(OptimizedProgramAdapter.ProgramState.Channel(channel = channel))
					.plus(programStates)
			}
		optimizedProgramAdapter.setItems(programAsTable)
	}


}