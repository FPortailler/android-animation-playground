package me.portailler.florian.testanimation.ui.designs.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.portailler.florian.testanimation.ui.designs.state.DesignNavState

@Composable
fun DesignCard(
	modifier: Modifier = Modifier,
	state: DesignNavState,
	onClick: (DesignNavState) -> Unit
) {
	Column(
		modifier = modifier
			.padding(4.dp)
			.background(
				color = Color(0xFF323232),
				shape = RoundedCornerShape(size = 8.dp)
			)
			.padding(16.dp)
			.wrapContentHeight(align = Alignment.CenterVertically),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = state.title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
		Text(text = state.description, color = Color.White, style = MaterialTheme.typography.bodySmall)
		Button(onClick = { onClick(state) }) {
			Text(text = "Open")
		}
	}
}