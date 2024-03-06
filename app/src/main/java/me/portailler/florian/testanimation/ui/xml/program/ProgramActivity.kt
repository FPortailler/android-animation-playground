package me.portailler.florian.testanimation.ui.xml.program

import android.os.Bundle
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.portailler.florian.testanimation.databinding.ProgramActivityBinding
import me.portailler.florian.testanimation.databinding.ProgramCellBinding
import me.portailler.florian.testanimation.databinding.ProgramChannelHeaderCellBinding
import me.portailler.florian.testanimation.ui.xml.program.logic.ProgramManager
import me.portailler.florian.testanimation.ui.xml.program.logic.ProgramManager.HALF_HOUR
import me.portailler.florian.testanimation.ui.xml.program.logic.ProgramManager.getSpan

class ProgramActivity : AppCompatActivity() {

	companion object {
		private const val TIME_STEP = HALF_HOUR
	}

	private lateinit var binding: ProgramActivityBinding


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ProgramActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		loadProgram()
	}


	private fun loadProgram() {
		lifecycleScope.launch {
			binding.programChannelList.removeAllViews()
			ProgramManager.programs
				.sortedBy { it.channel }
				.groupBy { it.channel }
				.forEach { (channel, programs) ->
					val row = TableRow(this@ProgramActivity)
					row.layoutParams = TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT
					)

					val channelCellBinding = ProgramChannelHeaderCellBinding.inflate(layoutInflater)
					channelCellBinding.programChannelHeaderCellName.text = "Ch $channel"
					binding.programChannelList.addView(channelCellBinding.root)


					programs.sortedBy { program ->
						program.startTime
					}.forEach { program ->
						val programCellBinding = ProgramCellBinding.inflate(layoutInflater)
						programCellBinding.programName.text = program.title
						// Note: we would be able to use a RecyclerView with a GridLayoutManager
						val layoutSpan: Long = program.getSpan()
						programCellBinding.root.layoutParams = TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,
							TableRow.LayoutParams.WRAP_CONTENT
						).apply { this.span = layoutSpan.toInt() }
						row.addView(programCellBinding.root)
					}
					binding.tableLayout.addView(row)

				}
		}
	}
}