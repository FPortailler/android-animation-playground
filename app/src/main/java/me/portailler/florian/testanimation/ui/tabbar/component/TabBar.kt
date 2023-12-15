package me.portailler.florian.testanimation.ui.tabbar.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.portailler.florian.testanimation.ui.tabbar.mock.TabBarItemProvider
import kotlin.math.max

@Composable
fun TabBar(
	modifier: Modifier = Modifier,
	itemCount: Int = 1,
	selectedIndex: Int = 0,
	indicator: @Composable (Modifier) -> Unit,
	windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
	content: @Composable RowScope.() -> Unit,
) {

	val density = LocalDensity.current
	var tabBarWidth by remember { mutableStateOf(0) }
	val xOffset by animateFloatAsState(
		targetValue = tabBarWidth * selectedIndex / max(1f, itemCount.toFloat()),
		label = "selectedIndex"
	)

	NoRipple {
		Box(
			modifier = modifier
				.fillMaxWidth()
				.windowInsetsPadding(windowInsets)
				.height(80.dp)
				.onGloballyPositioned { layoutCoordinates ->
					tabBarWidth = layoutCoordinates.size.width
				}
		) {
			if (itemCount == 0) return@Box
			indicator(
				modifier
					.fillMaxWidth(1f.div(itemCount))
					.offset(
						x = density.run { xOffset.toDp() },
						y = 0.dp
					)
			)
			Row(
				modifier = Modifier
					.selectableGroup(),
				content = {
					content()
				}
			)
		}
	}
}

@Preview
@Composable
private fun TabBarPreview() {
	val items = TabBarItemProvider.items
	var selectedIndex by remember { mutableStateOf(0) }
	TabBar(
		itemCount = items.size,
		selectedIndex = selectedIndex,
		indicator = { modifier ->
			Box(
				modifier = modifier
					.fillMaxHeight()
					.fillMaxWidth(1f)
			)
		},
		content = {
			items.forEachIndexed { index, item ->
				NavigationBarItem(
					modifier = Modifier
						.background(color = Color.Transparent),
					icon = { Icon(imageVector = if (selectedIndex == index) item.selectedIcon else item.icon, item.label) },
					label = { if (item.label != null) Text(item.label) },
					selected = false,
					onClick = { selectedIndex = index }
				)
			}
		}
	)
}