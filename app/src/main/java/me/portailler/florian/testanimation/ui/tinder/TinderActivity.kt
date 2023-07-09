package me.portailler.florian.testanimation.ui.tinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import kotlinx.coroutines.launch
import me.portailler.florian.testanimation.databinding.TinderActivityBinding
import me.portailler.florian.testanimation.databinding.TinderCardBinding
import me.portailler.florian.testanimation.ui.tinder.card.TinderAdapter
import me.portailler.florian.testanimation.ui.tinder.card.TinderCard
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntity
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.SWIPED_LEFT
import me.portailler.florian.testanimation.ui.tinder.utils.TinderViewUtils.SWIPED_RIGHT

class TinderActivity : AppCompatActivity() {


	private lateinit var binding: TinderActivityBinding

	private val viewModel: TinderViewModel by viewModels()
	private val adapter by lazy { TinderCardAdapter(::onSwipe) }

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

	private class TinderCardAdapter(
		private val onSwipeListener: (direction: Int, position: Int, item: TinderCardEntity) -> Unit
	) : TinderAdapter<TinderCardEntity>() {


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


	}

}