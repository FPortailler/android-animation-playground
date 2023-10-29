package me.portailler.florian.testanimation.ui.tinder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import kotlinx.coroutines.launch
import me.portailler.florian.testanimation.databinding.TinderActivityBinding
import me.portailler.florian.testanimation.databinding.TinderCardBinding
import me.portailler.florian.testanimation.ui.tinder.card.DragAndSwipeAdapter
import me.portailler.florian.testanimation.ui.tinder.card.TinderCard
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntity
import me.portailler.florian.testanimation.ui.tinder.utils.ViewUtils.SWIPED_LEFT
import me.portailler.florian.testanimation.ui.tinder.utils.ViewUtils.SWIPED_RIGHT
import me.portailler.florian.testanimation.ui.utils.AnimationUtils.fadeIn
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class TinderActivity : AppCompatActivity() {

	companion object {
		private const val SWIPE_THRESHOLD = 0.20f
	}

	private lateinit var binding: TinderActivityBinding

	private val viewModel: TinderViewModel by viewModels()
	private val adapter by lazy {
		DragAndSwipeCardAdapter(
			swipeThreshold = SWIPE_THRESHOLD,
			onSwipeListener = ::onSwipe,
			onSwipePercentUpdated = ::onSwipePercentUpdated
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = TinderActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		adapter.attachOn(binding.tinderCardPreview)

		lifecycleScope.launch {
			withCreated {
				launch {
					viewModel.entities.collect(::onEntitiesUpdate)
				}
			}
		}
		binding.swipeZoneLeft.alpha = 0f
		binding.swipeZoneRight.alpha = 0f
		viewModel.loadEntities()
	}

	private fun onEntitiesUpdate(entities: List<TinderCardEntity>) {
		adapter.replaceAll(entities)
	}

	private fun onSwipe(direction: Int, position: Int, item: TinderCardEntity) {
		when (direction) {
			SWIPED_LEFT -> {
				//TODO
			}

			SWIPED_RIGHT -> {
				//TODO
			}
		}
	}

	private fun onSwipePercentUpdated(percent: Float) {
		Log.d("TinderActivity", "onSwipePercentUpdated: percent = $percent")
		val relativePercent = (percent - 0.5f).coerceIn(-1 + SWIPE_THRESHOLD, 1 - SWIPE_THRESHOLD)
		val leftAlpha = abs(min(relativePercent, 0f) * 2f)
		val rightAlpha = max(relativePercent, 0f) * 2f
		Log.d("TinderActivity", "onSwipePercentUpdated: relativePercent = $relativePercent")
		Log.d("TinderActivity", "onSwipePercentUpdated: leftAlpha = $leftAlpha")
		Log.d("TinderActivity", "onSwipePercentUpdated: rightAlpha = $rightAlpha")
		binding.swipeZoneLeft.fadeIn(leftAlpha, 0)
		binding.swipeZoneRight.fadeIn(rightAlpha, 0)
	}

	private class DragAndSwipeCardAdapter(
		override var swipeThreshold: Float,
		private val onSwipeListener: (direction: Int, position: Int, item: TinderCardEntity) -> Unit,
		private val onSwipePercentUpdated: (percent: Float) -> Unit = {}
	) : DragAndSwipeAdapter<TinderCardEntity>() {

		override var stackSize: Int = 5

		fun replaceAll(entities: List<TinderCardEntity>) {
			this.data.clear()
			this.data.addAll(entities)
			notifyDataSetChanged()
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TinderCard {
			return TinderCard(TinderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
		}

		override fun getItemViewType(position: Int): Int = 0

		override fun onSwipe(direction: Int, position: Int, item: TinderCardEntity) = onSwipeListener(direction, position, item)

		override fun onCancel() = Unit

		override fun onSwipePercentUpdate(percent: Float) = onSwipePercentUpdated(percent)

	}

}