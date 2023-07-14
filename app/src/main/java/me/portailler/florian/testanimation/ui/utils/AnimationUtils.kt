package me.portailler.florian.testanimation.ui.utils

import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils {


	fun View.fadeIn(percent: Float, duration: Long = 300L) {
		ObjectAnimator.ofFloat(this, "alpha", percent.coerceIn(0f, 1f)).apply {
			this.duration = duration
			start()
		}
	}
}