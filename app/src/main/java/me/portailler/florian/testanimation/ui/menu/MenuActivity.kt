package me.portailler.florian.testanimation.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.portailler.florian.testanimation.core.navigation.UrlRouter
import me.portailler.florian.testanimation.databinding.MenuActivityBinding
import me.portailler.florian.testanimation.databinding.MenuCellBinding
import me.portailler.florian.testanimation.ui.menu.state.MenuDestination
import me.portailler.florian.testanimation.ui.menu.state.MenuDestinationProvider
import me.portailler.florian.testanimation.ui.shared.shake.ShakeListener

class MenuActivity : AppCompatActivity() {

	private lateinit var binding: MenuActivityBinding
	private val shakeListener by lazy { ShakeListener(::onShake, durationTrigger = 300) }
	private val adapter: MenuAdapter by lazy { MenuAdapter(onDestinationClicked = ::onDestinationClicked) }
	private val destinations = MenuDestinationProvider.items


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = MenuActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.menuRecyclerView.adapter = adapter
		adapter.replaceAll(destinations)
		shakeListener.start(this)
		checkIntent()
	}

	override fun onDestroy() {
		shakeListener.stop()
		super.onDestroy()
	}

	private fun checkIntent() {
		UrlRouter.handleUri(this, intent.data)
	}

	private fun onDestinationClicked(destination: MenuDestination) {
		startActivity(Intent(this, destination.activityClass))
	}

	private fun onShake() {
		Snackbar.make(binding.root, "Shake detected. Shake on each screen to toggle dev options", Snackbar.LENGTH_SHORT).show()
	}


	private class MenuAdapter(
		private val onDestinationClicked: (destination: MenuDestination) -> Unit
	) : RecyclerView.Adapter<MenuAdapter.MenuCell>() {

		private val data = mutableListOf<MenuDestination>()

		@SuppressLint("NotifyDataSetChanged")
		fun replaceAll(newData: List<MenuDestination>) {
			data.clear()
			data.addAll(newData)
			notifyDataSetChanged()
		}

		override fun getItemCount(): Int = data.size

		override fun onBindViewHolder(holder: MenuCell, position: Int) = data.getOrNull(position)?.let(holder::bind) ?: Unit

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCell = MenuCell(
			MenuCellBinding.inflate(LayoutInflater.from(parent.context), parent, false),
			onDestinationClicked = onDestinationClicked
		)

		private class MenuCell(
			private val binding: MenuCellBinding,
			private val onDestinationClicked: (destination: MenuDestination) -> Unit
		) : RecyclerView.ViewHolder(binding.root) {

			fun bind(destination: MenuDestination) {
				binding.menuCellTitle.text = destination.title
				binding.menuCellDescription.text = destination.description
				binding.root.setOnClickListener { onDestinationClicked(destination) }
			}

		}
	}
}
