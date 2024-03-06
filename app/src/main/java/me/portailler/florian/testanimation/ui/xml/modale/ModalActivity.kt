package me.portailler.florian.testanimation.ui.xml.modale

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.portailler.florian.testanimation.databinding.ModalActivityBinding
import me.portailler.florian.testanimation.databinding.MyModalFragmentBinding
import me.portailler.florian.testanimation.ui.xml.modale.helper.ModalHelper
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.finishAsModal
import me.portailler.florian.testanimation.ui.xml.modale.utils.ModalUtils.replaceAsModal
import me.portailler.florian.testanimation.ui.shared.BaseFragment

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
	private val fullscreen: Boolean
		get() = binding.modalActivityFullScreen.isChecked
	private val fullHeight: Boolean
		get() = binding.modalActivitySwitchFullHeight.isChecked
	private val midHeight: Boolean
		get() = binding.modalActivitySwitchMidHeight.isChecked
	private val lowHeight: Boolean
		get() = binding.modalActivitySwitchLowHeight.isChecked
	private val minimizeEnabled: Boolean
		get() = binding.modalActivitySwitchMinimize.isChecked

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
		val modalFragment = MyModalFragment.newInstance()
		modalFragment.modalHelper.fullscreen = fullscreen
		modalFragment.modalHelper.fullHeight = fullHeight
		modalFragment.modalHelper.midHeight = midHeight
		modalFragment.modalHelper.lowHeight = lowHeight
		modalFragment.modalHelper.minimizeEnabled = minimizeEnabled

		modalFragment.onDraggedOutListener = { supportFragmentManager.beginTransaction().remove(this).commit() }
		supportFragmentManager.replaceAsModal(binding.modalActivityFragmentContainer.id, modalFragment)
	}

	class MyModalFragment : BaseFragment<MyModalFragmentBinding>() {

		companion object {
			fun newInstance(): MyModalFragment = MyModalFragment()
		}

		var onDraggedOutListener: Fragment.() -> Unit = {}

		val modalHelper: ModalHelper<MyModalFragment> by lazy {
			ModalHelper(
				getHandle = { binding.modalFragmentHandle },
				getFullscreeenCloseButton = { binding.closeButton },
				onDraggedOut = { onDraggedOutListener(this) },
			)
		}

		override fun buildViewBinding(inflater: LayoutInflater, container: ViewGroup?): MyModalFragmentBinding {
			return MyModalFragmentBinding.inflate(layoutInflater, container, false)
		}

		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
			super.onViewCreated(view, savedInstanceState)
			modalHelper.attachTo(fragment = this)
		}
	}
}
