package me.portailler.florian.testanimation.ui.modale

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.portailler.florian.testanimation.databinding.ModalActivityBinding
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.finishAsModal
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.replaceAsModal

class ModalActivity : AppCompatActivity() {

	companion object {
		private const val EXTRA_AS_A_MODAL: String = "ModalActivity.EXTRA_AS_A_MODAL"
		fun getIntent(activity: AppCompatActivity, asAModal: Boolean = false): Intent {
			return Intent(activity, ModalActivity::class.java).apply {
				putExtra(EXTRA_AS_A_MODAL, asAModal)
			}
		}
	}

	private lateinit var binding: ModalActivityBinding
	private val modalFragment: ModalFragment by lazy { ModalFragment.newInstance() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ModalActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.openModalActivityButton.setOnClickListener { openModalActivity() }
		binding.openModalFragmentButton.setOnClickListener { openModalFragment() }
	}

	override fun onBackPressed() {
		finishAsModal()
		super.onBackPressed()
	}

	private fun openModalActivity() {
		startActivity(getIntent(this, asAModal = true))
	}

	private fun openModalFragment() {
		modalFragment.setOnDraggedOutListener { supportFragmentManager.beginTransaction().remove(it).commit() }
		supportFragmentManager.replaceAsModal(binding.modalActivityFragmentContainer.id, modalFragment)
	}

}
