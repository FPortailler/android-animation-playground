package me.portailler.florian.testanimation.ui.modale

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import me.portailler.florian.testanimation.databinding.ModalActivityBinding
import me.portailler.florian.testanimation.databinding.MyModalFragmentBinding
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_HALF_HEIGHT
import me.portailler.florian.testanimation.ui.modale.utils.ModalUtils.FLAG_MINIMIZABLE
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
	private val modalFragment: MyModalFragment by lazy {
		MyModalFragment.newInstance()
	}

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

	class MyModalFragment : ModalFragment() {

		companion object {
			fun newInstance(): MyModalFragment {
				val args = Bundle()
				val fragment = MyModalFragment()
				fragment.arguments = args
				return fragment
			}
		}

		private var _binding: MyModalFragmentBinding? = null

		override val flags: Int = ModalUtils.FLAG_FULL_HEIGHT or FLAG_HALF_HEIGHT or FLAG_MINIMIZABLE

		override fun createContentView(rootView: ViewGroup): View? {
			_binding = MyModalFragmentBinding.inflate(LayoutInflater.from(rootView.context), rootView, false)
			return _binding?.root
		}

		override fun onFullScreenSet() {
			super.onFullScreenSet()
			_binding?.closeButton?.isVisible = true
			_binding?.closeButton?.setOnClickListener { closeModal() }
		}
	}

}
