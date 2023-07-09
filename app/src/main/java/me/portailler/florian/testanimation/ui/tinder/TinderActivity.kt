package me.portailler.florian.testanimation.ui.tinder

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.TinderActivityBinding
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntity
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.SWIPED_LEFT
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.SWIPED_RIGHT
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.enableDragForCard

class TinderActivity : AppCompatActivity() {


	private lateinit var binding: TinderActivityBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = TinderActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		val entity = TinderCardEntity(
			id = "1",
			title = "Title",
			description = "Description"
		)
		binding.tinderCardContainer.enableDragForCard(
			threshold = 0.15f,
			onSwipe = ::onSwipe,
			onEnd = ::onSwipeAnimationEnd
		)
	}

	private fun onSwipe(direction: Int) {
		when (direction) {
			SWIPED_LEFT -> {
				//TODO
			}

			SWIPED_RIGHT -> {
				//TODO
			}
		}
	}

	private fun onSwipeAnimationEnd(view: View) {
		//TODO update the view with the next entity
	}

}