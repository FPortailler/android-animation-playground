package me.portailler.florian.testanimation.ui.program.component

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class TimeTableCell(root: View) : RecyclerView.ViewHolder(root) {

	abstract fun bind(state: TimeTableState): Unit
}