package me.portailler.florian.testanimation.ui.tinder.card

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import me.portailler.florian.testanimation.ui.tinder.utils.ViewUtils.enableDragAndSwipe

abstract class DragAndSwipeAdapter<T> {

	companion object {
		const val DEFAULT_EMPHASIS = 5f
		const val DEFAULT_SWIPE_THRESHOLD = 0.15f
		const val DEFAULT_STACK_SIZE = 2
	}

	/**
	 * Emphasis a factor to exaggerate the animation
	 */
	protected open var emphasis = DEFAULT_EMPHASIS

	/**
	 * Swipe threshold the percentage of the screen width to trigger the swipe
	 */
	protected open var swipeThreshold = DEFAULT_SWIPE_THRESHOLD

	protected open var stackSize = DEFAULT_STACK_SIZE

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

		val subData = data.take(stackSize)
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
						onSwipeStart = { direction -> onSwipeStart(direction, index, t) },
						onSwipeEnd = { direction -> onSwipeEnd(direction, index, t) },
						onSwipePercentUpdate = ::onSwipePercentUpdate,
						onCancel = ::onCancel
					)
				}
			}
		}

	}

	open fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = holder.bind(data[position], position)

	abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>

	abstract fun getItemViewType(position: Int): Int

	abstract fun onSwipeStart(direction: Int, position: Int, item: T)

	abstract fun onSwipeEnd(direction: Int, position: Int, item: T)

	abstract fun onSwipePercentUpdate(percent: Float)

	abstract fun onCancel()

	abstract class ViewHolder<T>(open val binding: ViewBinding) {

		abstract fun bind(item: T, position: Int)

	}
}
