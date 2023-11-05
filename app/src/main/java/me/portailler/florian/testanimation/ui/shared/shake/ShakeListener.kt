package me.portailler.florian.testanimation.ui.shared.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

/**
 * Shake listener
 * Heavily inspired from https://github.com/square/seismic/blob/master/library/src/main/java/com/squareup/seismic/ShakeDetector.java
 *
 * @property onShake
 * @property durationTrigger
 * @constructor Create empty Shake listener
 */
@Suppress("unused")
class ShakeListener(
	private val onShake: () -> Unit,
	/**
	 * Duration trigger in ms (default to 1_000L ms)
	 */
	var durationTrigger: Long = 1000L
) : SensorEventListener {

	companion object {
		private const val DEFAULT_ACCELERATION_THRESHOLD = 13
		private const val NO_SHAKE = -1L
	}

	private var accelerometer: Sensor? = null
	private var sensorManager: SensorManager? = null
	private val shakeBuffer: MutableList<Long> = mutableListOf()
	private var lastShakeInitiated: Long = NO_SHAKE

	private var accelerationThreshold: Int = DEFAULT_ACCELERATION_THRESHOLD


	fun start(context: Context): Boolean {
		if (accelerometer != null) return true
		sensorManager = context.getSystemService(SensorManager::class.java) ?: return false
		accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return false
		sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
		return true
	}

	fun stop() {
		sensorManager?.unregisterListener(this)
		sensorManager = null
		accelerometer = null
		lastShakeInitiated = NO_SHAKE
	}

	fun setSensitivity(sensitivity: Int = DEFAULT_ACCELERATION_THRESHOLD) {
		accelerationThreshold = sensitivity
	}

	override fun onSensorChanged(event: SensorEvent?) {
		if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
		if (isShaking(event)) {
			Log.d("ShakeListener", "Shaking")
			computeShakeDuration()
		} else noShakeDetected()
	}


	private fun isShaking(event: SensorEvent): Boolean {
		val (x, y, z) = event.values
		val acceleration = x * x + y * y + z * z
		return acceleration > accelerationThreshold * accelerationThreshold
	}

	private fun computeShakeDuration() {
		val now = System.currentTimeMillis()
		when {
			lastShakeInitiated == NO_SHAKE -> {
				Log.d("ShakeListener", "First shake")
				shakeBuffer.add(now)
				lastShakeInitiated = now
			}

			now - lastShakeInitiated > durationTrigger -> {
				Log.d("ShakeListener", "potential Shake detected")
				onShake()
				lastShakeInitiated = NO_SHAKE
				shakeBuffer.clear()
			}

			else -> shakeBuffer.add(now)
		}
		if (lastShakeInitiated == NO_SHAKE) {
			Log.d("ShakeListener", "First shake")
			lastShakeInitiated = now
			shakeBuffer.add(now)
			return
		}
		if (now - lastShakeInitiated > durationTrigger) {
			Log.d("ShakeListener", "Shake confirmed")
			onShake()
			lastShakeInitiated = now
		}
	}

	private fun noShakeDetected() {
		if (shakeBuffer.isEmpty()) return
		shakeBuffer.add(NO_SHAKE)
		if (shakeBuffer.all { it == NO_SHAKE }
			|| lastShakeInitiated != NO_SHAKE && System.currentTimeMillis() - lastShakeInitiated > durationTrigger
		) {
			Log.d("ShakeListener", "Shake reset")
			shakeBuffer.clear()
			lastShakeInitiated = NO_SHAKE
		}
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
