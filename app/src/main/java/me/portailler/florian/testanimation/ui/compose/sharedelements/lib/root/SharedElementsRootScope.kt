package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root

import androidx.compose.runtime.staticCompositionLocalOf

interface SharedElementsRootScope {
	val isRunningTransition: Boolean
	var selectedIndex: Int
	var previousSelectedIndex: Int
	fun prepareTransition(vararg elements: Any)
}

fun SharedElementsRootScope.select(index: Int, provideElements: (index: Int) -> List<Any>) {
	val currentIndex = selectedIndex
	if (currentIndex != index) {
		val targetIndex = if (index >= 0) index else currentIndex
		if (targetIndex >= 0) {
			prepareTransition(provideElements(targetIndex))
		}
		previousSelectedIndex = selectedIndex
		selectedIndex = index
	}
}

val LocalSharedElementsRootScope = staticCompositionLocalOf<SharedElementsRootScope?> { null }

