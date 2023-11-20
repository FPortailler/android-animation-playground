package me.portailler.florian.testanimation.ui.shortcuts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import me.portailler.florian.testanimation.R
import me.portailler.florian.testanimation.ui.shortcuts.screen.ShortcutScreen


class ShortcutActivity : ComponentActivity() {

	companion object {
		private const val EXTRA_ACCESSED_FROM_SHORTCUT = "ShortcutActivity.EXTRA_ACCESSED_FROM_SHORTCUT"

		fun prepare(context: Context, accessedFromShortCut: Boolean): Intent = Intent(context, ShortcutActivity::class.java)
			.putExtra(EXTRA_ACCESSED_FROM_SHORTCUT, accessedFromShortCut)
			.setAction("com.android.launcher.action.INSTALL_SHORTCUT")
			.putExtra("duplicate", true)
	}

	private val accessedFromShortcut by lazy { intent.getBooleanExtra(EXTRA_ACCESSED_FROM_SHORTCUT, false) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				ShortcutScreen(
					modifier = Modifier.fillMaxSize(),
					accessedFromShortCut = accessedFromShortcut,
					onAddShortcutClicked = ::addShortcut
				)
			}
		}
	}

	private fun addShortcut() {
		val activityIntent = prepare(applicationContext, accessedFromShortCut = true)
		val infoCompat = ShortcutInfoCompat.Builder(applicationContext, "123")
			.setShortLabel("Shortcut")
			.setLongLabel("Access Shortcut Activity")
			.setIcon(IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground))
			.setLongLived(true)
			.setIntent(activityIntent)
			.build()
		ShortcutManagerCompat.requestPinShortcut(applicationContext, infoCompat, null)
	}
}