package com.yahboom.tank.control

object ControlConfig {
    private const val BASE_URL = "http://192.168.50.1:5000"

    const val FORWARD = "$BASE_URL/forward"
    const val BACKWARD = "$BASE_URL/backward"
    const val LEFT = "$BASE_URL/left"
    const val RIGHT = "$BASE_URL/right"
    const val STOP = "$BASE_URL/stop"

    const val CAMERA_UP = "$BASE_URL/camera/up"
    const val CAMERA_DOWN = "$BASE_URL/camera/down"
    const val CAMERA_LEFT = "$BASE_URL/camera/left"
    const val CAMERA_RIGHT = "$BASE_URL/camera/right"
    const val CAMERA_CENTER = "$BASE_URL/camera/center"
}
