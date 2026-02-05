package com.yahboom.tank.stream

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MjpegStreamView(
    url: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MjpegSurfaceView(context).apply { startStream(url) }
        },
        update = { view ->
            view.startStream(url)
        }
    )
}
