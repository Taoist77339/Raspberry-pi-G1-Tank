package com.yahboom.tank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yahboom.tank.control.TankController
import com.yahboom.tank.stream.MjpegStreamView
import com.yahboom.tank.ui.theme.YahboomTankTheme

private const val DEFAULT_STREAM_URL = "http://192.168.50.1:8080/?action=stream"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YahboomTankTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    StreamSurface()
                }
            }
        }
    }
}

@Composable
private fun StreamSurface() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        MjpegStreamView(
            url = DEFAULT_STREAM_URL,
            modifier = Modifier.fillMaxSize()
        )
        StreamOverlay()
    }
}

@Composable
private fun StreamOverlay() {
    val controller = remember { TankController() }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "MJPEG: 192.168.50.1:8080",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )

        CameraControls(
            controller = controller,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 36.dp, end = 12.dp)
        )

        DriveControls(
            controller = controller,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        )
    }
}

@Composable
private fun DriveControls(controller: TankController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { controller.forward() }) { Text("Forward") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { controller.left() }) { Text("Left") }
            Button(onClick = { controller.stop() }) { Text("Stop") }
            Button(onClick = { controller.right() }) { Text("Right") }
        }
        Button(onClick = { controller.backward() }) { Text("Backward") }
    }
}

@Composable
private fun CameraControls(controller: TankController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Camera", color = Color.White)
        Button(onClick = { controller.cameraUp() }) { Text("↑") }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(onClick = { controller.cameraLeft() }) { Text("←") }
            Button(onClick = { controller.cameraCenter() }) { Text("•") }
            Button(onClick = { controller.cameraRight() }) { Text("→") }
        }
        Button(onClick = { controller.cameraDown() }) { Text("↓") }
    }
}
