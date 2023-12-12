package me.portailler.florian.testanimation.ui.program

import android.os.Bundle
import android.support.annotation.ColorRes
import android.util.Log
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.databinding.ProgramActivityBinding
import me.portailler.florian.testanimation.databinding.ProgramCellBinding
import me.portailler.florian.testanimation.ui.program.logic.ProgramEntity
import me.portailler.florian.testanimation.ui.program.logic.ProgramManager
import me.portailler.florian.testanimation.ui.program.logic.ProgramManager.HALF_HOUR
import kotlin.math.max

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
		ProgramManager.programs.groupBy { it.channel }
			.forEach { (channel, programs) ->
				val row = TableRow(this)
				row.id = channel.ordinal
				row.layoutParams = TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT
				)
				@ColorRes val channelColor: Int = when (channel) {
					ProgramEntity.Channel.TF1 -> R.color.tf1
					ProgramEntity.Channel.FRANCE_2 -> R.color.france_2
					ProgramEntity.Channel.FRANCE_3 -> R.color.france_3
					ProgramEntity.Channel.FRANCE_5 -> R.color.france_5
					ProgramEntity.Channel.FRANCE_O -> R.color.france_o

				}

				programs.sortedBy { program ->
					program.startTime
				}.forEach { program ->
					val programCellBinding = ProgramCellBinding.inflate(layoutInflater)
					programCellBinding.programName.text = program.title
					programCellBinding.programCell.setBackgroundColor(ContextCompat.getColor(this, channelColor))
					val layoutSpan: Long = max(1, (program.endTime - program.startTime) / TIME_STEP)
					programCellBinding.root.layoutParams = TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT,
					).apply {
						this.span = layoutSpan.toInt()
						Log.d("ProgramActivity", "layoutSpan: ${layoutSpan.toInt()} for program ${program.title}")
					}
					row.addView(programCellBinding.root)
				}
				binding.tableLayout.addView(row)

			}
	}
}