package me.portailler.florian.testanimation.core.navigation

import android.content.Context
import android.net.Uri
import me.portailler.florian.testanimation.ui.shortcuts.ShortcutActivity

object UrlRouter {

	fun handleUri(context: Context, data: Uri?): Boolean {
		if(data == null) return false
		return when (data.scheme) {
			"testanimation" -> when (data.host) {
//				"tinderCompose" -> {
//					context.startActivity(TinderComposeActivity.prepare(context))
//					true
//				}
				"shortcut" -> {
					context.startActivity(ShortcutActivity.prepare(context, accessedFromShortCut = data.getBooleanQueryParameter("accessedFromShortcut", false)))
					true
				}

				else -> false
			}

			else -> false
		}
	}
}