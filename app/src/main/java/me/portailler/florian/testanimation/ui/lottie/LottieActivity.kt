package me.portailler.florian.testanimation.ui.lottie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.LottieActivityBinding

class LottieActivity : AppCompatActivity() {

	private lateinit var binding: LottieActivityBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = LottieActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}