package me.portailler.florian.testanimation.ui.tinder.card

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.enableDragForCard

abstract class TinderAdapter<T> {

	protected var emphasis = 5f
	protected var swipeThreshold = 0.15f

	private var frameLayout: ViewGroup? = null
	private val viewHolders = mutableMapOf<Int, List<ViewHolder<T>>>()
	val data: MutableList<T> = mutableListOf()

	fun attachOn(viewGroup: ConstraintLayout) {
		this.frameLayout = viewGroup
	}

	protected fun notifyDataSetChanged() {
		val parent = frameLayout ?: return
		parent.removeAllViews()
		if (data.isEmpty()) return

		val subData = data.take(2)
		subData.forEachIndexed { index, t ->
			with(getItemViewType(index)) {
				val topViewHolders = viewHolders.getOrPut(this) { emptyList() }.toMutableList()
				if (topViewHolders.isEmpty()) topViewHolders += onCreateViewHolder(parent, this)
				var viewHolder = topViewHolders.find { !it.binding.root.isAttachedToWindow }
				if (viewHolder == null) {
					viewHolder = onCreateViewHolder(parent, this)
					topViewHolders += viewHolder
				}
				viewHolder.apply {
					onBindViewHolder(this, index)
					parent.addView(binding.root)
					binding.root.elevation = subData.lastIndex - index.toFloat()
					if (index == 0) binding.root.enableDragForCard(
						emphasis = emphasis,
						threshold = swipeThreshold,
						onSwipe = { direction -> onSwipe(direction, index, t) },
						onEnd = {
							data.removeAt(0)
							notifyDataSetChanged()
						}
					)
				}
			}
		}

	}

	abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>

	open fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
		val item = data[position]
		holder.bind(item)
	}

	abstract fun getItemViewType(position: Int): Int

	abstract fun onSwipe(direction: Int, position: Int, item: T)

	abstract class ViewHolder<T>(open val binding: ViewBinding) {

		abstract fun bind(item: T)

	}
}