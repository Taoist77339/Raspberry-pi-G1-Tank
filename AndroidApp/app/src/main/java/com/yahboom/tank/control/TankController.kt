package com.yahboom.tank.control

import java.net.HttpURLConnection
import java.net.URL

class TankController {
    fun forward() = send(ControlConfig.FORWARD)
    fun backward() = send(ControlConfig.BACKWARD)
    fun left() = send(ControlConfig.LEFT)
    fun right() = send(ControlConfig.RIGHT)
    fun stop() = send(ControlConfig.STOP)

    fun cameraUp() = send(ControlConfig.CAMERA_UP)
    fun cameraDown() = send(ControlConfig.CAMERA_DOWN)
    fun cameraLeft() = send(ControlConfig.CAMERA_LEFT)
    fun cameraRight() = send(ControlConfig.CAMERA_RIGHT)
    fun cameraCenter() = send(ControlConfig.CAMERA_CENTER)

    private fun send(url: String) {
        Thread {
            try {
                val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 1500
                    readTimeout = 1500
                }
                connection.inputStream.use { it.readBytes() }
                connection.disconnect()
            } catch (_: Exception) {
            }
        }.start()
    }
}
