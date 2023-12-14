package me.portailler.florian.testanimation.ui.program.component

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class TimeTableAdapter(
	private val span: Int,
	private val isFirstColumHeader: Boolean,
) : RecyclerView.Adapter<TimeTableCell>() {

	open val cells = mutableListOf<TimeTableState>()

	open fun setItems(table: List<List<TimeTableState>>) {
		cells.clear()
		table.forEach { row ->
			if (!isFirstColumHeader && row.isEmpty()) {
				cells.add(TimeTableState.empty(span, 0))
				return@forEach
			}
			var lastSpanPosition = if (isFirstColumHeader) 0 else row.firstOrNull()?.startAt ?: 0
			row.forEach { item ->
				if (item.startAt > lastSpanPosition) cells.add(TimeTableState.empty(item.startAt - lastSpanPosition, lastSpanPosition))
				cells.add(item)
				lastSpanPosition = item.startAt + item.span
			}
			if (lastSpanPosition + 1 < span) cells.add(TimeTableState.empty(span - lastSpanPosition - 1, lastSpanPosition))
		}
		notifyDataSetChanged()
	}

	open fun buildSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
		return object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				return cells.getOrNull(position)?.span ?: 1
			}
		}
	}

	override fun getItemCount(): Int = cells.size

	override fun onBindViewHolder(holder: TimeTableCell, position: Int) = cells.getOrNull(position)?.let(holder::bind) ?: Unit


}