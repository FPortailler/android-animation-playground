package me.portailler.florian.testanimation.ui.tinder.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

object TinderViewUtils {

	const val SWIPED_LEFT = -1
	const val NOT_SWIPED = 0
	const val SWIPED_RIGHT = 1

	@SuppressLint("ClickableViewAccessibility")
	fun View.enableDragForCard(
		emphasys: Float = 1f,
		threshold: Float = 0.15f,
		onSwipe: (direction: Int) -> Unit = {},
		onEnd: (View) -> Unit = {}
	) {
		var touchedDown = false
		var startEvent: MotionEvent = MotionEvent.obtain(0, 0, 0, 0f, 0f, 0)
		setOnTouchListener { v, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (!touchedDown) startEvent = MotionEvent.obtain(event)
					touchedDown = true
					true
				}

				MotionEvent.ACTION_MOVE -> {
					if (touchedDown) tilt(startEvent, MotionEvent.obtain(event), emphasys = emphasys)
					true
				}

				MotionEvent.ACTION_UP -> {
					touchedDown = false
					when (isSwipedOut(event, threshold)) {
						SWIPED_LEFT -> {
							moveOut(SWIPED_LEFT, startEvent, event, onEnd)
							onSwipe(SWIPED_LEFT)
						}

						SWIPED_RIGHT -> {
							moveOut(SWIPED_RIGHT, startEvent, event, onEnd)
							onSwipe(SWIPED_RIGHT)
						}

						NOT_SWIPED -> {
							v.reset()
						}
					}
					true
				}

				else -> false
			}
		}
	}

	private fun View.isSwipedOut(event: MotionEvent, threshold: Float): Int {
		val parentView = (parent as ViewGroup)
		return when {
			event.rawX < (parentView.right.toFloat() * threshold) -> SWIPED_LEFT
			event.rawX > (parentView.right.toFloat() * (1 - threshold)) -> SWIPED_RIGHT
			else -> NOT_SWIPED
		}
	}

	private fun View.tilt(startEvent: MotionEvent, event: MotionEvent, emphasys: Float = 1f) {
		if (!isEventRealistic(event)) return
		pivotX = width / 2f
		pivotY = height * 1.25f
		val dragXPercent = (event.rawX - startEvent.rawX) / startEvent.x
		val dragYPercent = (event.rawY - startEvent.rawY) / startEvent.y
		val rotation = emphasys * dragXPercent
		val translationX = startEvent.x * dragXPercent
		val translationY = startEvent.y * dragYPercent
		if (translationX.isInfinite() || translationY.isInfinite()) return
		if (translationX.isNaN() || translationY.isNaN()) return
		Log.d("TinderViewUtils", "rotation: $rotation, translationX: $translationX, translationY: $translationY")

		val tiltAnimation = AnimatorSet()
		tiltAnimation.duration = 0
		val rotationAnimation = ObjectAnimator.ofFloat(
			this,
			View.ROTATION,
			rotation,
		)
		val translationXAnimation = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_X,
			translationX
		)
		val translationYAnimation = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_Y,
			translationY,
		)

		tiltAnimation.playTogether(
			translationXAnimation,
			rotationAnimation,
			translationYAnimation
		)
		tiltAnimation.start()
	}

	private fun View.reset() {
		post {
			translationX = 0f
			translationY = 0f
			rotation = 0f
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				animationMatrix = null
			} else {
				rotation = 0f
				translationX = 0f
				translationY = 0f
			}
		}
	}

	private fun View.moveOut(direction: Int, startEvent: MotionEvent, event: MotionEvent, onEnd: (View) -> Unit) {
		val dx = direction * (this.context as Activity).window.decorView.width.toFloat()
		val targetY = startEvent.targetY(event, dx)
		val xyAnimation = AnimatorSet()
		val xAnimation = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_X,
			dx,
		)

		val yAnimation = ObjectAnimator.ofFloat(
			this,
			View.TRANSLATION_Y,
			targetY,
		)

		xyAnimation.playTogether(xAnimation, yAnimation)


		xyAnimation.addListener(object : AnimatorListenerAdapter() {
			override fun onAnimationEnd(animation: Animator) {
				animation.removeListener(this)
				onEnd(this@moveOut)
				reset()
			}
		})
		xyAnimation.start()
	}

	private fun MotionEvent.targetY(event: MotionEvent, dx: Float): Float {
		val factor = (event.rawX - rawX) / (event.rawX + dx - rawX)
		return (event.rawY - rawY) / factor - (event.rawY - rawY)
	}

	private val View.centerX
		get() = width.toFloat() / 2f + left.toFloat()

	private val View.centerY
		get() = height.toFloat() / 2f + top.toFloat()

	private fun View.isEventRealistic(event: MotionEvent): Boolean {
		return (context as? Activity)?.window?.decorView?.let {
			val x = event.rawX
			val y = event.rawY
			return@let x > it.left && x < it.right && y > it.top && y < it.bottom
		} ?: false
	}
}