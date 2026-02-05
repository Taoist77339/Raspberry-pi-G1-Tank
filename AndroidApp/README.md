# Android App (Step 1) - Stream + Drive + Camera Movement Controls

This folder contains a bootstrapped Android app for the Yahboom tank controller.

## What is included
- A minimal **Jetpack Compose** app that renders an MJPEG stream from the tank.
- A basic MJPEG surface view implementation for HTTP stream playback.
- A drive control pad (Forward/Left/Stop/Right/Backward).
- A camera movement pad (Up/Down/Left/Right/Center).
- A configurable control URL map in `control/ControlConfig.kt`.

## Default stream URL
Configured in `MainActivity`:
http://192.168.50.1:8080/?action=stream
