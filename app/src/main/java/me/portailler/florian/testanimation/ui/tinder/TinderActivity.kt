package me.portailler.florian.testanimation.ui.tinder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.rotationMatrix
import me.portailler.florian.testanimation.databinding.TinderActivityBinding
import me.portailler.florian.testanimation.ui.tinder.card.TinderCardEntity

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
		enableDragForCard(entity)
	}


	@SuppressLint("ClickableViewAccessibility")
	private fun enableDragForCard(entity: TinderCardEntity) {
		binding.tinderCardContainer.apply {
			var touchedDown = false
			var startEvent: MotionEvent? = null
			setOnTouchListener { v, event ->
				when (event.action) {
					MotionEvent.ACTION_DOWN -> {
						touchedDown = true
						startEvent = MotionEvent.obtain(event)
						true
					}

					MotionEvent.ACTION_MOVE -> {
						if (touchedDown) startEvent?.let { v.tilt(it, event, emphasys = 2f) }
						true
					}

					MotionEvent.ACTION_UP -> {
						v.reset()
						touchedDown = false
						true
					}

					else -> false
				}
			}
		}
	}

	private fun View.tilt(startEvent: MotionEvent, event: MotionEvent, emphasys: Float = 1f) {
		val dragXPercent = (event.x - startEvent.x) / startEvent.x
		val dragYPercent = (event.y - startEvent.y) / startEvent.y
		pivotX = width / 2f
		pivotY = height * 1.24f
		val rotationMatrix = rotationMatrix(
			dragXPercent * emphasys,
			pivotX,
			pivotY
		)
		rotationMatrix.preTranslate(this.x * dragXPercent, this.y * dragYPercent * emphasys)
		animationMatrix = rotationMatrix
	}

	private fun View.reset() {
		animationMatrix = null
	}

}