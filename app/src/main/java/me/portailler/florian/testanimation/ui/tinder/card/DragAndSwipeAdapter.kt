package me.portailler.florian.testanimation.ui.tinder.card

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import me.portailler.florian.testanimation.ui.tinder.utils.ViewUtils.enableDragAndSwipe

abstract class DragAndSwipeAdapter<T> {

	/**
	 * Emphasis a factor to exaggerate the animation
	 */
	protected open var emphasis = 5f

	/**
	 * Swipe threshold the percentage of the screen width to trigger the swipe
	 */
	protected open var swipeThreshold = 0.15f

	private var parentView: ViewGroup? = null
	private val viewHolders = mutableMapOf<Int, List<ViewHolder<T>>>()
	val data: MutableList<T> = mutableListOf()

	/**
	 * Attach on
	 *
	 * @param viewGroup [ViewGroup] the parent view in which the views will be added.
	 * It needs to have paddings to allow the views to be swiped out of it
	 * Clip to padding will be disabled
	 */
	fun attachOn(viewGroup: ViewGroup) {
		this.parentView = viewGroup
		viewGroup.clipToPadding = false
	}

	protected fun notifyDataSetChanged() {
		val parent = parentView ?: return
		parent.removeAllViews()
		if (data.isEmpty()) return

		val subData = data.take(2)
		subData.forEachIndexed { index, t ->
			with(getItemViewType(index)) {
				// Get the top available view holder
				val topViewHolders = viewHolders.getOrPut(this) { emptyList() }.toMutableList()
				if (topViewHolders.isEmpty()) topViewHolders += onCreateViewHolder(parent, this)
				var viewHolder = topViewHolders.find { !it.binding.root.isAttachedToWindow }
				if (viewHolder == null) {
					viewHolder = onCreateViewHolder(parent, this)
					topViewHolders += viewHolder
				}
				viewHolder.apply {
					// Bind the view holder to the data
					onBindViewHolder(this, index)
					// Add the view to the parent
					parent.addView(binding.root)
					binding.root.elevation = subData.lastIndex - index.toFloat()
					// Enable drag and swipe on the first view
					if (index == 0) binding.root.enableDragAndSwipe(
						emphasis = emphasis,
						threshold = swipeThreshold,
						onSwipe = { direction -> onSwipe(direction, index, t) },
						onEnd = {
							data.removeAt(0)
							notifyDataSetChanged()
						},
						onSwipePercentUpdate = ::onSwipePercentUpdate,
						onCancel = ::onCancel
					)
				}
			}
		}

	}

	open fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = holder.bind(data[position])

	abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>

	abstract fun getItemViewType(position: Int): Int

	abstract fun onSwipe(direction: Int, position: Int, item: T)

	abstract fun onSwipePercentUpdate(percent: Float)

	abstract fun onCancel()

	abstract class ViewHolder<T>(open val binding: ViewBinding) {

		abstract fun bind(item: T)

	}
}