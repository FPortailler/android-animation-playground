package me.portailler.florian.testanimation

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.MainActivityBinding
import me.portailler.florian.testanimation.ui.home.HomeFragment
import me.portailler.florian.testanimation.ui.shared.ArticlesViewModel

class MainActivity : AppCompatActivity() {

	private lateinit var binding: MainActivityBinding

	private val viewModel: ArticlesViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = MainActivityBinding.inflate(layoutInflater)
		supportRequestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
		setContentView(binding.root)
		supportFragmentManager.beginTransaction()
			.add(binding.mainFragmentContainer.id, HomeFragment(), HomeFragment::class.java.simpleName)
			.commit()
	}


}