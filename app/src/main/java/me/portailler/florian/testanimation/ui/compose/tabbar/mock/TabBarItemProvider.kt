package me.portailler.florian.testanimation.ui.compose.tabbar.mock

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import me.portailler.florian.testanimation.ui.compose.tabbar.state.TabBarItem

object TabBarItemProvider {

	val items by lazy {
		listOf(
			TabBarItem(
				icon = Icons.Outlined.Home,
				selectedIcon = Icons.Filled.Home,
				label = "Home",
			),
			TabBarItem(
				icon = Icons.Outlined.FavoriteBorder,
				selectedIcon = Icons.Filled.Favorite,
				label = "Favorite",
			),
			TabBarItem(
				icon = Icons.Outlined.Search,
				selectedIcon = Icons.Filled.Search,
				label = "Search",
			),
			TabBarItem(
				icon = Icons.Outlined.Settings,
				selectedIcon = Icons.Filled.Settings,
				label = "Settings",
			),
			TabBarItem(
				icon = Icons.Outlined.AccountCircle,
				selectedIcon = Icons.Filled.AccountCircle,
				label = "Profile",
			),
		)
	}
}