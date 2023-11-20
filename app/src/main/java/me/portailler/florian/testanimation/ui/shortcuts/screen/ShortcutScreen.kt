package me.portailler.florian.testanimation.ui.shortcuts.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ShortcutScreen(
	modifier: Modifier = Modifier,
	accessedFromShortCut: Boolean = false,
	onAddShortcutClicked: () -> Unit = {}
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = if (accessedFromShortCut) "Accessed from shortcut" else "Not accessed from shortcut",
			color = Color.White
		)
		if (!accessedFromShortCut) {
			Button(onClick = { onAddShortcutClicked() }) {
				Text(text = "Add shortcut")
			}
		}
	}
}

@Preview
@Composable
private fun ShortcutScreenPreview() {
	MaterialTheme {
		ShortcutScreen()
	}
}