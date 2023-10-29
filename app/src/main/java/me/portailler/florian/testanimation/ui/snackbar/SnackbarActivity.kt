package me.portailler.florian.testanimation.ui.snackbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.SnackbarActivityBinding
import me.portailler.florian.testanimation.databinding.SnackbarItemBinding
import me.portailler.florian.testanimation.ui.snackbar.component.DismissAdapter
import me.portailler.florian.testanimation.ui.snackbar.component.SnackbarViewHolder
import me.portailler.florian.testanimation.ui.snackbar.logic.SnackbarEntity
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SnackbarActivity : AppCompatActivity() {

	private lateinit var binding: SnackbarActivityBinding
	private val adapter by lazy { SnackbarAdatper(onActionClicked = ::onActionClicked) }
	private val random = Random(1)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = SnackbarActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.recyclerView.adapter = this.adapter

		binding.showMessageButton.setOnClickListener {
			val action = if (random.nextBoolean()) "Dismiss" else null
			val snackbar = SnackbarEntity(
				title = "Message ${System.currentTimeMillis().toDuration(DurationUnit.MILLISECONDS)}",
				action = action
			)
			adapter.addOnTop(snackbar)
		}
	}

	private fun onActionClicked(snackbarEntity: SnackbarEntity) {
	}


	private class SnackbarAdatper(
		private val onActionClicked: (SnackbarEntity) -> Unit
	) : DismissAdapter<SnackbarEntity>() {
		override fun getDismissDelay(item: SnackbarEntity): Long = if (item.action != null) -1 else 3000

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DismissableViewHolder<SnackbarEntity> {
			return SnackbarViewHolder(
				SnackbarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
				onActionClicked = onActionClicked
			)
		}

		override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()
	}
}