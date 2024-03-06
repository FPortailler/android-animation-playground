package me.portailler.florian.testanimation.ui.xml.snackbar.component

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


abstract class DismissAdapter<T> : RecyclerView.Adapter<DismissAdapter.DismissableViewHolder<T>>() {

	private val autoDismissSupervisor = SupervisorJob()
	private val autoDismissJobs = mutableListOf<Job>()

	private val snackbars = mutableListOf<T>()

	protected fun getItem(position: Int): T? = snackbars.getOrNull(position)

	fun addOnTop(item: T) {
		snackbars.add(0, item)
		val delay = getDismissDelay(item)
		if (delay > 0) {
			val job = CoroutineScope(Dispatchers.Main).launch(autoDismissSupervisor) {
				delay(delay)

				dismissItem(item)
			}
			autoDismissJobs.add(job)
		}
		notifyItemInserted(0)
	}

	private fun dismissItem(item: T) {
		val index = snackbars.indexOf(item)
		if (index >= 0) {
			snackbars.removeAt(index)
			notifyItemRemoved(index)
		}
	}

	abstract fun getDismissDelay(item: T): Long

	override fun getItemCount(): Int = snackbars.size

	override fun getItemViewType(position: Int): Int = 0

	override fun onBindViewHolder(holder: DismissableViewHolder<T>, position: Int) = snackbars.getOrNull(position)?.let {
		holder.bindWithDismiss(it, ::dismissItem)
	} ?: Unit

	abstract class DismissableViewHolder<T>(
		binding: ViewBinding,
	) : RecyclerView.ViewHolder(binding.root) {
		fun bindWithDismiss(item: T, dismiss: (T) -> Unit) {
			this.dismiss = dismiss
			bind(item)
		}

		abstract fun bind(item: T)

		var dismiss: (T) -> Unit = {}
			private set
	}

}
