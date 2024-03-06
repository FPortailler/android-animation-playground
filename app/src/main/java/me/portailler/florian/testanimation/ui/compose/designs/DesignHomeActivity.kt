package me.portailler.florian.testanimation.ui.compose.designs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.portailler.florian.testanimation.ui.compose.designs.shared.DesignCard
import me.portailler.florian.testanimation.ui.compose.designs.state.DesignNavProvider

class DesignHomeActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				Navigator(HomeScreen())
			}
		}
	}

	private class HomeScreen : Screen {
		@Composable
		override fun Content() {
			val navigator = LocalNavigator.currentOrThrow
			LazyVerticalGrid(columns = GridCells.Fixed(2)) {
				items(DesignNavProvider.states) { state ->
					DesignCard(state = state) {
						navigator.push(state.screen)
					}
				}
			}
		}
	}
}