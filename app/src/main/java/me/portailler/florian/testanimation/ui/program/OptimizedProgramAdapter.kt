package me.portailler.florian.testanimation.ui.program

import android.view.LayoutInflater
import android.view.ViewGroup
import me.portailler.florian.testanimation.databinding.ProgramCellBinding
import me.portailler.florian.testanimation.databinding.ProgramChannelHeaderCellBinding
import me.portailler.florian.testanimation.databinding.ProgramEmptyCellBinding
import me.portailler.florian.testanimation.ui.program.component.TimeTableAdapter
import me.portailler.florian.testanimation.ui.program.component.TimeTableCell
import me.portailler.florian.testanimation.ui.program.component.TimeTableState
import me.portailler.florian.testanimation.ui.program.logic.ProgramEntity

class OptimizedProgramAdapter(
	span: Int,
) : TimeTableAdapter(span, isFirstColumHeader = true) {

	companion object {
		private const val VIEW_TYPE_CHANNEL_HEADER = 0
		private const val VIEW_TYPE_PROGRAM = 1
		private const val VIEW_TYPE_EMPTY = 2
	}


	override fun getItemViewType(position: Int): Int = when (cells.getOrNull(position)) {
		is ProgramState.Channel -> VIEW_TYPE_CHANNEL_HEADER
		is ProgramState.Program -> VIEW_TYPE_PROGRAM
		is TimeTableState.Empty -> VIEW_TYPE_EMPTY
		else -> throw IllegalArgumentException("Unknown viewType $position")
	}
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableCell = when (viewType) {
		VIEW_TYPE_PROGRAM -> ProgramCell(
			ProgramCellBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

		VIEW_TYPE_CHANNEL_HEADER -> ChannelCell(
			ProgramChannelHeaderCellBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

		VIEW_TYPE_EMPTY -> EmptyCell(
			ProgramEmptyCellBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

		else -> throw IllegalArgumentException("Unknown viewType $viewType")
	}

	private class ProgramCell(
		private val binding: ProgramCellBinding
	) : TimeTableCell(binding.root) {

		override fun bind(state: TimeTableState) {
			val program = (state as? ProgramState.Program)?.program ?: return
			binding.programName.text = program.title
			binding.programCell.setBackgroundColor(state.color)
		}
	}

	private class ChannelCell(
		private val binding: ProgramChannelHeaderCellBinding
	) : TimeTableCell(binding.root) {

		override fun bind(state: TimeTableState) {
			val channel = (state as? ProgramState.Channel)?.channel ?: return
			binding.programChannelHeaderCellName.text = "Ch $channel"
		}
	}

	private class EmptyCell(
		binding: ProgramEmptyCellBinding
	) : TimeTableCell(binding.root) {
		override fun bind(state: TimeTableState): Unit = Unit
	}

	sealed class ProgramState(
		override val span: Int,
		override val startAt: Int,
	) : TimeTableState() {
		class Program(
			val program: ProgramEntity,
			span: Int,
			startAt: Int,
			val color: Int,
			override val isRowHeader: Boolean = false,
			override val isColumnHeader: Boolean = false,
		) : ProgramState(span, startAt)

		class Channel(
			val channel: Int,
			override val isRowHeader: Boolean = true,
			override val isColumnHeader: Boolean = false,
		) : ProgramState(span = 1, startAt = 0)
	}
}