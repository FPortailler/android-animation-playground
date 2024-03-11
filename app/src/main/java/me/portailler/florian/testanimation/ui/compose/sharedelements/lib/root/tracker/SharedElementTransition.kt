package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.tracker

import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.PositionedSharedElement

internal sealed class SharedElementTransition(val startElement: PositionedSharedElement) {

	class WaitingForEndElementPosition(startElement: PositionedSharedElement) :
		SharedElementTransition(startElement)

	class InProgress(
		startElement: PositionedSharedElement,
		val endElement: PositionedSharedElement,
		val onTransitionFinished: () -> Unit
	) : SharedElementTransition(startElement)

}
