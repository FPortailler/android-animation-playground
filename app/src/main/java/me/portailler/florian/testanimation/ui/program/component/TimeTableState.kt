package me.portailler.florian.testanimation.ui.program.component

abstract class TimeTableState {
	abstract val span: Int
	abstract val startAt: Int
	abstract val isRowHeader: Boolean
	abstract val isColumnHeader: Boolean

	class Empty internal constructor(
		override val span: Int,
		override val startAt: Int,
		override val isRowHeader: Boolean = false,
		override val isColumnHeader: Boolean = false
	) : TimeTableState()

	companion object {
		fun empty(span: Int, start: Int) = Empty(span, start)
	}
}