package me.portailler.florian.testanimation.ui.modale.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.portailler.florian.testanimation.R

object ModalUtils {

	private const val DEFAULT_DROP_DOWN_PERCENT_THRESHOLD: Float = 0.6f

	fun FragmentManager.replaceAsModal(@IdRes fragmentContainer: Int, fragment: Fragment) = beginTransaction()
		.setCustomAnimations(R.anim.slide_in_to_top, android.R.anim.fade_out, android.R.anim.fade_in, R.anim.slide_out_from_top)
		.replace(fragmentContainer, fragment)
		.commit()

	private val isSDK33OrAbove: Boolean
		get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

	private val isSDK34OrAbove: Boolean
		get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

	fun Activity.finishAsModal(@ColorRes backgroundColor: Int = android.R.color.black) {
		finish()
		when {
			isSDK34OrAbove -> overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, android.R.anim.fade_out, R.anim.slide_out_from_top, getColor(backgroundColor))
			isSDK33OrAbove -> overridePendingTransition(android.R.anim.fade_out, R.anim.slide_out_from_top, backgroundColor)
			else -> overridePendingTransition(android.R.anim.fade_out, R.anim.slide_out_from_top)
		}
	}

	fun Activity.openAsModal(@ColorRes backgroundColor: Int = android.R.color.black) {
		when {
			isSDK34OrAbove -> this.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_to_top, android.R.anim.fade_out, getColor(backgroundColor))
			isSDK33OrAbove -> this.overridePendingTransition(R.anim.slide_in_to_top, android.R.anim.fade_out, backgroundColor)
			else -> this.overridePendingTransition(R.anim.slide_in_to_top, android.R.anim.fade_out)
		}
		this.overridePendingTransition(R.anim.slide_in_to_top, android.R.anim.fade_out)
	}

	@SuppressLint("ClickableViewAccessibility")
	fun View.setHandleBehavior(
		targetView: View,
		dropDownThreshold: Float = DEFAULT_DROP_DOWN_PERCENT_THRESHOLD,
		onDraggedOut: () -> Unit,
		onDragCancelled: () -> Unit,
	) {
		var touchDown = false
		var startY = 0f
		setOnTouchListener { v, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (!touchDown) startY = event.rawY
					touchDown = true
					true
				}

				MotionEvent.ACTION_MOVE -> {
					if (touchDown) {
						val deltaY = event.rawY - startY
						targetView.dragTo(y = deltaY, animationDelay = 0L)
					}
					true
				}

				MotionEvent.ACTION_UP -> {
					touchDown = false
					if (targetView.isSwipedOut(event, dropDownThreshold)) targetView.swipeOut {
						onDraggedOut()
						targetView.isVisible = false
						targetView.dragTo(0F, animationDelay = 0)
					}
					else targetView.dragTo(y = 0F, animationDelay = 300, onAnimationCompleted = onDragCancelled)
					true
				}

				else -> false
			}
		}
	}

	private fun View.isSwipedOut(event: MotionEvent, dropDownThreshold: Float): Boolean {
		val swipePercent = event.rawY / bottom.toFloat()
		return swipePercent > dropDownThreshold
	}

	private fun View.swipeOut(
		animationDelay: Long = 300,
		onAnimationCompleted: (() -> Unit)? = null
	) = dragTo(
		y = (this.context as Activity).window.decorView.height.toFloat(),
		animationDelay = animationDelay,
		onAnimationCompleted = onAnimationCompleted
	)


	private fun View.dragTo(
		y: Float,
		animationDelay: Long,
		onAnimationCompleted: (() -> Unit)? = null
	) {
		val animator = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_Y,
			y
		)
		animator.addListener(object : AnimatorListenerAdapter() {
			override fun onAnimationEnd(animation: Animator) {
				super.onAnimationEnd(animation)
				animator.removeListener(this)
				onAnimationCompleted?.invoke()
			}
		})

		animator.duration = animationDelay
		animator.start()
	}

}
