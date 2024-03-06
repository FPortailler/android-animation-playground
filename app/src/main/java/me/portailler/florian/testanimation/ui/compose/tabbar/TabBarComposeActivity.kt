package me.portailler.florian.testanimation.ui.compose.tabbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.portailler.florian.testanimation.ui.compose.tabbar.component.TabBar
import me.portailler.florian.testanimation.ui.compose.tabbar.mock.TabBarItemProvider
import me.portailler.florian.testanimation.ui.compose.tabbar.state.TabBarItem

class TabBarComposeActivity : ComponentActivity() {

	private val selectionColor = Color(0xFFC6E2EE)
	private val unselectedColor = Color(0xFF767683)
	private val backgroundTabBarColor = Color(0xFF0F0E12)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			MaterialTheme {
				val items = TabBarItemProvider.items
				var selectedItem: Int by remember { mutableStateOf(0) }

				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(
							brush = Brush.verticalGradient(
								0f to Color(0xFF333333),
								1f to Color(0xFFCCCCCC)
							)
						),
					verticalArrangement = Arrangement.Bottom,
				) {
					//Tab bar with FAB
					Box {
						TabBar(
							Modifier
								.background(
									color = backgroundTabBarColor,
								),
							itemCount = items.size,
							selectedIndex = selectedItem,
							indicator = { modifier ->
								Surface(
									modifier = modifier
										.fillMaxHeight()
										.padding(16.dp),
									color = if (selectedItem != items.size / 2) selectionColor.copy(alpha = .2f) else Color.Transparent,
									shape = RoundedCornerShape(16.dp),
								) {}
							}
						) {
							BuildTabBarItems(items, selectedItem, hideIndexForFab = items.size / 2, showLabel = false) { selectedItem = it }
						}
						Box(
							modifier = Modifier
								.size(50.dp)
								.align(Alignment.Center)
								.background(
									color = selectionColor,
									shape = CircleShape
								)
								.clickable {
									selectedItem = items.size / 2
								},
						) {
							Icon(
								modifier = Modifier
									.align(Alignment.Center)
									.size(24.dp),
								imageVector = items[items.size / 2].selectedIcon,
								contentDescription = items[items.size / 2].label,
							)
						}
					}
					//Floating tab bar
					Box(modifier = Modifier.padding(16.dp)) {

						TabBar(
							Modifier
								.background(
									color = backgroundTabBarColor,
									shape = RoundedCornerShape(16.dp)
								),
							itemCount = items.size,
							selectedIndex = selectedItem,
							indicator = { modifier ->
								Surface(
									modifier = modifier
										.fillMaxHeight()
										.padding(16.dp),
									color = selectionColor.copy(alpha = .2f),
									shape = RoundedCornerShape(16.dp),
								) {}
							}
						) {
							BuildTabBarItems(items, selectedItem, showLabel = false) { selectedItem = it }
						}
					}
					//Tab bar with higher fab
					Box {
						TabBar(
							Modifier
								.align(Alignment.BottomStart)
								.background(
									color = backgroundTabBarColor,
								),
							itemCount = items.size,
							selectedIndex = selectedItem,
							indicator = { modifier ->
								Surface(
									modifier = modifier
										.fillMaxHeight()
										.padding(16.dp),
									color = if (selectedItem != items.size / 2) selectionColor.copy(alpha = .2f) else Color.Transparent,
									shape = RoundedCornerShape(16.dp),
								) {}
							}
						) {
							BuildTabBarItems(items, selectedItem, hideIndexForFab = items.size / 2, showLabel = false) { selectedItem = it }
						}
						Box(
							modifier = Modifier
								.padding(bottom = 8.dp)
								.align(Alignment.TopCenter)
								.size(82.dp)
								.background(
									color = backgroundTabBarColor,
									shape = CircleShape
								)
								.padding(16.dp)
								.background(
									color = selectionColor,
									shape = CircleShape
								)
								.clickable {
									selectedItem = items.size / 2
								},
						) {
							Icon(
								modifier = Modifier
									.align(Alignment.Center)
									.size(24.dp),
								imageVector = items[items.size / 2].selectedIcon,
								contentDescription = items[items.size / 2].label,
							)
						}
					}
					// Top Line indicator
					TabBar(
						Modifier.background(color = backgroundTabBarColor),
						itemCount = items.size,
						selectedIndex = selectedItem,
						indicator = { modifier ->
							Surface(
								modifier = modifier
									.height(2.dp),
								color = selectionColor,
							) {}
						}
					) {
						BuildTabBarItems(items, selectedItem) { selectedItem = it }
					}
					// Top to bottom gradient indicator
					TabBar(
						Modifier.background(color = backgroundTabBarColor),
						itemCount = items.size,
						selectedIndex = selectedItem,
						indicator = { modifier ->
							Box(
								modifier = modifier
									.fillMaxHeight()
									.background(
										brush = Brush.verticalGradient(
											0f to selectionColor,
											1f to Color.Transparent
										),
										alpha = 0.3f
									)
							)
						}
					) {
						BuildTabBarItems(items, selectedItem) { selectedItem = it }
					}
					// Rounded square indicator
					TabBar(
						Modifier.background(color = backgroundTabBarColor),
						itemCount = items.size,
						selectedIndex = selectedItem,
						indicator = { modifier ->
							Surface(
								modifier = modifier
									.fillMaxHeight()
									.padding(16.dp),
								color = selectionColor.copy(alpha = .2f),
								shape = RoundedCornerShape(16.dp)
							) {}
						}
					) {
						BuildTabBarItems(items, selectedItem, showLabel = false) { selectedItem = it }
					}
					//Dot indicator
					TabBar(
						Modifier.background(color = backgroundTabBarColor),
						itemCount = items.size,
						selectedIndex = selectedItem,
						indicator = { modifier ->
							Box(
								modifier = modifier
									.fillMaxHeight()
							) {
								Surface(
									modifier = Modifier
										.align(Alignment.BottomCenter)
										.padding(bottom = 16.dp)
										.size(4.dp),
									shape = CircleShape,
									color = selectionColor
								) {}
							}
						}
					) {
						BuildTabBarItems(items, selectedItem, showLabel = false) { selectedItem = it }
					}
				}
			}
		}
	}


	@Composable
	private fun RowScope.BuildTabBarItems(
		items: List<TabBarItem>,
		selectedIndex: Int,
		hideIndexForFab: Int? = null,
		showLabel: Boolean = true,
		onClick: (Int) -> Unit
	) {
		items.forEachIndexed { index, item ->
			NavigationBarItem(
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.background(color = Color.Transparent),
				icon = {
					Icon(
						imageVector = when (selectedIndex) {
							index -> item.selectedIcon
							else -> item.icon
						},
						item.label,
						tint = when {
							hideIndexForFab != null && hideIndexForFab == index -> Color.Transparent
							selectedIndex == index -> Color.White
							else -> unselectedColor
						}
					)
				},
				label = {
					if (item.label != null) Text(
						item.label,
						color = when {
							hideIndexForFab != null && hideIndexForFab == index -> Color.Transparent
							selectedIndex == index -> Color.White
							else -> unselectedColor
						}
					)
				},
				alwaysShowLabel = showLabel && item.label != null,
				selected = false,
				onClick = { onClick(index) },
			)
		}
	}
}