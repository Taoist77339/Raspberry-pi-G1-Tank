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
```
http://192.168.50.1:8080/?action=stream
```

## Control endpoint assumptions
The original repository does not include the runtime Pi control server code/APIs, so the app currently assumes placeholder HTTP endpoints:

### Drive
- `http://192.168.50.1:5000/forward`
- `http://192.168.50.1:5000/backward`
- `http://192.168.50.1:5000/left`
- `http://192.168.50.1:5000/right`
- `http://192.168.50.1:5000/stop`

### Camera movement
- `http://192.168.50.1:5000/camera/up`
- `http://192.168.50.1:5000/camera/down`
- `http://192.168.50.1:5000/camera/left`
- `http://192.168.50.1:5000/camera/right`
- `http://192.168.50.1:5000/camera/center`

Update these in `ControlConfig.kt` to match your actual robot command service.

## Next steps
1. Replace placeholder control endpoints with confirmed command API/protocol from your Pi runtime.
2. Add press-and-hold behavior so movement commands repeat while holding buttons.
3. Add camera tuning controls (exposure, white balance, FPS, bitrate) once endpoint support is confirmed.

