package me.portailler.florian.testanimation.ui.sharedelement.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.ArticleFragmentBinding

class ArticleActivity : AppCompatActivity() {

	private lateinit var binding: ArticleFragmentBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ArticleFragmentBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}