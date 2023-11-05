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
import kotlin.math.abs

object ModalUtils {

	private const val FULL_HEIGHT_THRESHOLD: Float = 0f
	private const val HALF_HEIGHT_THRESHOLD: Float = 0.5f
	private const val LOW_HEIGHT_THRESHOLD: Float = 0.66f
	private const val MINIMIZE_THRESHOLD: Float = 0.85f

	const val FLAG_FULL_SCREEN: Int = 1
	const val FLAG_FULL_HEIGHT: Int = 2
	const val FLAG_HALF_HEIGHT: Int = 4
	const val FLAG_LOW_HEIGHT: Int = 8
	const val FLAG_MINIMIZABLE: Int = 16

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

	//TODO handle FLAG_FULL_SCREEN
	//TODO add FLAG_WRAP_CONTENT (incompatible with the others)
	//TODO compute height instead of translation ?
	@SuppressLint("ClickableViewAccessibility")
	@Throws(IllegalArgumentException::class)
	fun View.setHandleBehavior(
		targetView: View,
		onDraggedOut: () -> Unit,
		onDragRelease: (DragLevel) -> Unit,
		flags: Int = FLAG_FULL_HEIGHT
	) {
		checkFlagValidity(flags = flags)
		var touchDown = false
		var startY = 0f
		var originalYTranslation: Float = 0f
		setOnTouchListener { v, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (!touchDown) {
						startY = event.rawY
						originalYTranslation = targetView.translationY
					}
					touchDown = true
					true
				}

				MotionEvent.ACTION_MOVE -> {
					if (touchDown) {
						val deltaY = event.rawY - startY
						targetView.dragTo(originalYTranslation, y = deltaY, animationDelay = 0L)
					}
					true
				}

				MotionEvent.ACTION_UP -> {
					touchDown = false
					when (val nextDragLevel = targetView.computeDragLevel(event, flags)) {
						DragLevel.GONE_HEIGHT -> targetView.swipeOut(originalYTranslation) {
							onDraggedOut()
							targetView.isVisible = false
							targetView.dragTo(originalYTranslation, y = 0F, animationDelay = 0)
						}

						else -> {
							val y = (this.context as Activity).window.decorView.height.toFloat() * nextDragLevel.heightRatio
							targetView.dragTo(originalYTranslation = 0f, y = y, animationDelay = 300, onAnimationCompleted = { onDragRelease(nextDragLevel) })
						}
					}
					true
				}

				else -> false
			}
		}
	}

	//TODO add dy to prefer gesture target instead of raw nearest value
	private fun View.computeDragLevel(
		event: MotionEvent,
		flags: Int,
	): DragLevel {
		val fullHeight: Boolean = flags and FLAG_FULL_HEIGHT == FLAG_FULL_HEIGHT
		val halfHeight: Boolean = flags and FLAG_HALF_HEIGHT == FLAG_HALF_HEIGHT
		val lowHeight: Boolean = flags and FLAG_LOW_HEIGHT == FLAG_LOW_HEIGHT
		val minimizedHeight: Boolean = flags and FLAG_MINIMIZABLE == FLAG_MINIMIZABLE
		val swipePercent = event.rawY / bottom.toFloat()
		val thresholds: MutableList<DragLevel> = mutableListOf(DragLevel.GONE_HEIGHT)
		if (fullHeight) thresholds.add(DragLevel.FULL_HEIGHT)
		if (halfHeight) thresholds.add(DragLevel.HALF_HEIGHT)
		if (lowHeight) thresholds.add(DragLevel.THIRD_HEIGHT)
		if (minimizedHeight) thresholds.add(DragLevel.MINIMIZED_HEIGHT)
		return thresholds.reduce { acc, threshold -> if (abs(acc.heightRatio - swipePercent) < abs(threshold.heightRatio - swipePercent)) acc else threshold }
	}

	private fun View.swipeOut(
		originalYTranslation: Float,
		animationDelay: Long = 300,
		onAnimationCompleted: (() -> Unit)? = null
	) = dragTo(
		originalYTranslation = originalYTranslation,
		y = (this.context as Activity).window.decorView.height.toFloat(),
		animationDelay = animationDelay,
		onAnimationCompleted = onAnimationCompleted
	)


	private fun View.dragTo(
		originalYTranslation: Float,
		y: Float,
		animationDelay: Long,
		onAnimationCompleted: (() -> Unit)? = null
	) {
		val animator = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_Y,
			y + originalYTranslation
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

	@Throws(IllegalArgumentException::class)
	private fun checkFlagValidity(flags: Int) {
		check(flags % 2 == 0 || flags == 1) { throw IllegalStateException("Incompatible flags : FLAG_FULL_SCREEN is not compatible with other flags") }
	}

	enum class DragLevel(val heightRatio: Float) {
		FULL_HEIGHT(FULL_HEIGHT_THRESHOLD),
		HALF_HEIGHT(HALF_HEIGHT_THRESHOLD),
		THIRD_HEIGHT(LOW_HEIGHT_THRESHOLD),
		MINIMIZED_HEIGHT(MINIMIZE_THRESHOLD),
		GONE_HEIGHT(1f)
	}

}
