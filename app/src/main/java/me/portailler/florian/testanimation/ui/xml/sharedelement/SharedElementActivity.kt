package me.portailler.florian.testanimation.ui.xml.sharedelement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.databinding.MainActivityBinding
import me.portailler.florian.testanimation.ui.menu.MenuActivity
import me.portailler.florian.testanimation.ui.xml.sharedelement.home.HomeFragment

class SharedElementActivity : AppCompatActivity() {

	private lateinit var binding: MainActivityBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = MainActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		supportFragmentManager.beginTransaction()
			.setReorderingAllowed(true)
			.add(R.id.mainFragmentContainer, HomeFragment(), HomeFragment::class.java.simpleName)
			.addToBackStack(HomeFragment::class.java.simpleName)
			.commit()

		binding.fabMenu.setOnClickListener { startActivity(Intent(this, MenuActivity::class.java)) }
	}

	override fun onBackPressed() {
		Log.d("BACK", "${supportFragmentManager.backStackEntryCount} fragments in backstack")
		supportFragmentManager.popBackStack()
//		supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)?.let { fragment ->
//			supportFragmentManager.beginTransaction()
//				.replace(R.id.mainFragmentContainer, fragment)
//				.commit()
//		}
		super.onBackPressed()
	}

}
