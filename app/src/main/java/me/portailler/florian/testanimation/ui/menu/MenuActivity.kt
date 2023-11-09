package me.portailler.florian.testanimation.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.portailler.florian.testanimation.databinding.MenuActivityBinding
import me.portailler.florian.testanimation.databinding.MenuCellBinding
import me.portailler.florian.testanimation.ui.joystick.JoystickActivity
import me.portailler.florian.testanimation.ui.lottie.LottieActivity
import me.portailler.florian.testanimation.ui.lottie.LottieComposeActivity
import me.portailler.florian.testanimation.ui.menu.state.MenuDestination
import me.portailler.florian.testanimation.ui.modale.ModalActivity
import me.portailler.florian.testanimation.ui.shared.shake.ShakeListener
import me.portailler.florian.testanimation.ui.sharedelement.SharedElementActivity
import me.portailler.florian.testanimation.ui.snackbar.SnackbarActivity
import me.portailler.florian.testanimation.ui.tinder.TinderActivity
import me.portailler.florian.testanimation.ui.tinderCompose.TinderComposeActivity

class MenuActivity : AppCompatActivity() {

	private lateinit var binding: MenuActivityBinding
	private val shakeListener by lazy { ShakeListener(::onShake, durationTrigger = 300) }
	private val adapter: MenuAdapter by lazy { MenuAdapter(onDestinationClicked = ::onDestinationClicked) }
	private val destinations = listOf(
		MenuDestination(
			title = "Tinder",
			description = "Swipe cards",
			activityClass = TinderActivity::class.java
		),
		MenuDestination(
			title = "Tinder Compose",
			description = "Swipe cards now in compose",
			activityClass = TinderComposeActivity::class.java
		),
		MenuDestination(
			title = "Shared Element",
			description = "Image transition from a list to a detail",
			activityClass = SharedElementActivity::class.java
		),
		MenuDestination(
			title = "Joystick",
			description = "Just a joystick composable",
			activityClass = JoystickActivity::class.java
		),
		MenuDestination(
			title = "Snackbar",
			description = "A snackbar with a custom animation",
			activityClass = SnackbarActivity::class.java
		),
		MenuDestination(
			title = "Modal",
			description = "An attempt to make iOS modal look and feel",
			activityClass = ModalActivity::class.java
		),
		MenuDestination(
			title = "Lottie",
			description = "A lottie animation",
			activityClass = LottieActivity::class.java
		),
		MenuDestination(
			title = "Lottie Compose",
			description = "A lottie animation in compose",
			activityClass = LottieComposeActivity::class.java
		)
	)


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = MenuActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.menuRecyclerView.adapter = adapter
		adapter.replaceAll(destinations)
		shakeListener.start(this)
	}

	override fun onDestroy() {
		shakeListener.stop()
		super.onDestroy()
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
