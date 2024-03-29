package me.portailler.florian.testanimation.ui.xml.tinder.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

object ViewUtils {

	private const val FIFTY_PERCENT = 0.5f
	private const val NOT_SWIPED = 0
	private const val PIVOT_POINT_VERTICAL_FACTOR = 1.25f
	private const val DEFAULT_DRAG_THRESHOLD = 50f
	const val SWIPED_LEFT = -1
	const val SWIPED_RIGHT = 1

	/**
	 * Enable drag and swipe
	 *
	 * @param emphasis a factor to exaggerate the animation
	 * @param threshold the percentage of the screen width to trigger the swipe
	 * @param onSwipeStart callback when the card is swiped [SWIPED_LEFT] or [SWIPED_RIGHT]
	 * @param onSwipeEnd callback when the card is swiped and the animation is finished
	 * (allows to update the view before it enters the screen again)
	 * @param onCancel callback when the card is not swiped enough to trigger [onSwipeStart]
	 * @param onSwipePercentUpdate callback when the card is swiped, gives the percentage of the screen width swiped
	 * @param onSingleTap callback when the card is tapped
	 * @param dragThreshold the threshold in pixels to trigger the drag (does not scale with accessibility settings)
	 */
	@SuppressLint("ClickableViewAccessibility")
	fun View.enableDragAndSwipe(
		emphasis: Float = 1f,
		threshold: Float = 0.15f,
		onSwipeStart: (direction: Int) -> Unit = {},
		onSwipeEnd: (direction: Int) -> Unit = {},
		onCancel: () -> Unit = {},
		onSwipePercentUpdate: (percent: Float) -> Unit = { },
		onSingleTap: () -> Unit = {},
		dragThreshold: Float = DEFAULT_DRAG_THRESHOLD,
	) {
		var touchedDown = false
		var didDragStart = false
		var startEvent: MotionEvent = MotionEvent.obtain(0, 0, 0, 0f, 0f, 0)
		setOnTouchListener { v, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (!touchedDown) startEvent = MotionEvent.obtain(event)
					touchedDown = true
					didDragStart = false
					true
				}

				MotionEvent.ACTION_MOVE -> {
					if (touchedDown) {
						val currentEvent = MotionEvent.obtain(event)
						if (startEvent.checkIsDraggedTo(currentEvent, dragThreshold)) {
							didDragStart = true
							tilt(startEvent, currentEvent, emphasys = emphasis)
							onSwipePercentUpdate(swipePercent(currentEvent))
						}
					}
					true
				}

				MotionEvent.ACTION_UP -> {
					touchedDown = false
					if (!didDragStart) onSingleTap()
					else {
						when (isSwipedOut(event, threshold)) {
							SWIPED_LEFT -> {
								moveOut(SWIPED_LEFT, startEvent, event, onSwipeEnd)
								onSwipeStart(SWIPED_LEFT)
							}

							SWIPED_RIGHT -> {
								moveOut(SWIPED_RIGHT, startEvent, event, onSwipeEnd)
								onSwipeStart(SWIPED_RIGHT)
							}

							NOT_SWIPED -> {
								onCancel()
								v.reset()
							}
						}
						onSwipePercentUpdate(FIFTY_PERCENT)
					}
					true
				}

				else -> false
			}
		}
	}

	private fun View.isSwipedOut(event: MotionEvent, threshold: Float): Int {
		val percent = swipePercent(event)
		return when {
			percent < threshold -> SWIPED_LEFT
			percent > (1 - threshold) -> SWIPED_RIGHT
			else -> NOT_SWIPED
		}
	}

	private fun View.swipePercent(event: MotionEvent): Float = (event.rawX / (parent as ViewGroup).right.toFloat())

	private fun View.tilt(startEvent: MotionEvent, event: MotionEvent, emphasys: Float = 1f) {
		if (!isEventRealistic(event)) return
		pivotX = width / 2f
		pivotY = height * PIVOT_POINT_VERTICAL_FACTOR
		val dragXPercent = (event.rawX - startEvent.rawX) / startEvent.rawX
		val dragYPercent = (event.rawY - startEvent.rawY) / startEvent.rawY
		val rotation = emphasys * dragXPercent
		val translationX = startEvent.rawX * dragXPercent
		val translationY = startEvent.rawY * dragYPercent
		if (translationX.isInfinite() || translationY.isInfinite()) return
		if (translationX.isNaN() || translationY.isNaN()) return

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

	//FixMe: memorize initial transformations in order to preserve them on the reset
	private fun View.reset(duration: Long = 100) {
		post {
			val tiltAnimation = AnimatorSet()
			tiltAnimation.duration = duration
			val rotationAnimation = ObjectAnimator.ofFloat(
				this,
				View.ROTATION,
				0f,
			)
			val translationXAnimation = ObjectAnimator.ofFloat(
				this,
				View.TRANSLATION_X,
				0f
			)
			val translationYAnimation = ObjectAnimator.ofFloat(
				this,
				View.TRANSLATION_Y,
				0f,
			)

			tiltAnimation.playTogether(
				translationXAnimation,
				rotationAnimation,
				translationYAnimation
			)
			tiltAnimation.start()
		}
	}

	private fun View.moveOut(direction: Int, startEvent: MotionEvent, event: MotionEvent, onSwipeEnd: (direction: Int) -> Unit) {
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
				onSwipeEnd(direction)
				reset(0)
			}
		})
		xyAnimation.start()
	}

	private fun MotionEvent.targetY(event: MotionEvent, dx: Float): Float {
		val factor = (event.rawX - rawX) / (event.rawX + dx - rawX)
		return (event.rawY - rawY) / factor - (event.rawY - rawY)
	}

	private fun View.isEventRealistic(event: MotionEvent): Boolean {
		return (context as? Activity)?.window?.decorView?.let {
			val x = event.rawX
			val y = event.rawY
			return@let x > it.left && x < it.right && y > it.top && y < it.bottom
		} ?: false
	}

	private fun MotionEvent.checkIsDraggedTo(target: MotionEvent, threshold: Float): Boolean {
		val distanceSquared: Float = (rawX - target.rawX) * (rawX - target.rawX) + (rawY - target.rawY) * (rawY - target.rawY)
		return distanceSquared > threshold * threshold
	}
}
