package me.portailler.florian.testanimation.ui.compose.changeicon

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import me.portailler.florian.testanimation.ui.compose.changeicon.state.IconState

class ChangeIconActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			MaterialTheme {
				Box {
					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						modifier = Modifier.fillMaxSize()
					) {
						items(IconState.values()) { iconState ->
							Image(
								modifier = Modifier
									.fillMaxSize()
									.clickable {
										changeIcon(iconState)
									},
								painter = painterResource(iconState.icon),
								contentDescription = "Home"
							)
						}
					}
				}
			}
		}
	}

	private fun changeIcon(iconState: IconState) {
		IconState.values().forEach { state ->
			packageManager.setComponentEnabledSetting(
				ComponentName(
					this,
					state.alias
				),
				if (iconState == state) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP
			)
		}
	}
}