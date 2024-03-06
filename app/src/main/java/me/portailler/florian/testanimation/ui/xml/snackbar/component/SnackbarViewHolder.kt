package me.portailler.florian.testanimation.ui.xml.snackbar.component

import androidx.core.view.isVisible
import me.portailler.florian.testanimation.databinding.SnackbarItemBinding
import me.portailler.florian.testanimation.ui.xml.snackbar.logic.SnackbarEntity


class SnackbarViewHolder(
	private val binding: SnackbarItemBinding,
	private val onActionClicked: (SnackbarEntity) -> Unit,
) : DismissAdapter.DismissableViewHolder<SnackbarEntity>(binding) {
	override fun bind(item: SnackbarEntity) {
		binding.message.text = item.title
		binding.action.apply {
			isVisible = item.action != null
			text = item.action
			setOnClickListener {
				onActionClicked(item)
				dismiss(item)
			}
		}
	}
}
