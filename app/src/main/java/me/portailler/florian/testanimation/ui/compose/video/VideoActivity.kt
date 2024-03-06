package me.portailler.florian.testanimation.ui.compose.video

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			VideoScreen()
		}
	}
}

@Composable
private fun VideoScreen(
	modifier: Modifier = Modifier.fillMaxSize(),
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {

		VideoPlayer(
			modifier = Modifier.fillMaxWidth(),
			mediaItem = MediaItem.fromUri("https://storage.googleapis.com/gweb-uniblog-publish-prod/original_videos/SURFACING__V1_DROID.mp4")
		)
	}
}


@OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(
	modifier: Modifier = Modifier,
	exoPlayer: ExoPlayer = ExoPlayer.Builder(LocalContext.current).build(),
	mediaItem: MediaItem,
) {

	LaunchedEffect(key1 = mediaItem) {
		exoPlayer.setMediaItem(mediaItem)
		exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
		exoPlayer.prepare()
		exoPlayer.play()
	}

	DisposableEffect(Unit) {
		onDispose {
			exoPlayer.release()
		}
	}

	AndroidView(
		factory = { context ->
			PlayerView(context).apply {
				player = exoPlayer
			}
		},
		modifier = modifier
	)
}
