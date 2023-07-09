package me.portailler.florian.testanimation.ui.tinder.card

import me.portailler.florian.testanimation.databinding.TinderCardBinding
import me.portailler.florian.testanimation.loadAndSetImage

class TinderCard(
	override val binding: TinderCardBinding
) : TinderAdapter.ViewHolder<TinderCardEntity>(binding) {

	override fun bind(item: TinderCardEntity) {
		binding.title.text = item.title
		binding.description.text = item.description
		binding.imageView.loadAndSetImage(item.url)
	}
}