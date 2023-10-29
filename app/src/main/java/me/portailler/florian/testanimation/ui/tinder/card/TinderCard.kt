package me.portailler.florian.testanimation.ui.tinder.card

import android.widget.FrameLayout
import me.portailler.florian.testanimation.databinding.TinderCardBinding
import me.portailler.florian.testanimation.loadAndSetImage

class TinderCard(
	override val binding: TinderCardBinding
) : DragAndSwipeAdapter.ViewHolder<TinderCardEntity>(binding) {

	override fun bind(item: TinderCardEntity, position: Int) {
		binding.title.text = item.title
		binding.description.text = item.description
		binding.imageView.loadAndSetImage(item.url)
		if (position < 1) {
			binding.root.elevation = 0F
			return
		}
		val shift = (position - 1) * 50
		binding.root.translationY = shift.toFloat()
	}
}