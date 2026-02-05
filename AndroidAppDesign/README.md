# Android App Design: Yahboom Tank Controller

## Goals
- Provide a modern Android controller app with **full camera/video control** for the Yahboom tank.
- Support low-latency streaming, camera tuning, recording, snapshots, and pan/tilt control.
- Provide a clean, extensible architecture so new robot features (AI modes, sensors, telemetry) can be added quickly.

## Primary Use Cases
1. **Drive + watch video** (low-latency, stable stream).
2. **Tune camera** (exposure, white balance, focus, gain, FPS, bitrate, resolution, color profile).
3. **Record video / take snapshots** while driving.
4. **Switch camera sources** (USB camera, Pi camera, or multiple camera profiles).
5. **Network resilience** (auto-reconnect, adaptive bitrate, quick profile switch).

---

## Functional Requirements

### Video & Camera Controls
- **Stream control**: start/stop, reconnect, transport toggle (RTSP/HTTP/WebRTC).
- **Resolution/FPS presets**: 640×480@30, 1280×720@15, 1920×1080@10.
- **Bitrate control**: slider with safe ranges; show estimated bandwidth.
- **Manual camera controls**:
  - Exposure: auto/manual + shutter speed (if supported).
  - White balance: auto/manual + temperature slider.
  - Focus: auto/manual + focus distance slider.
  - Gain/ISO: slider with limits.
  - Brightness/contrast/saturation.
- **Image enhancements**: toggle denoise, sharpening, color profile presets.
- **Recording**: start/stop recording on device (with timestamps) + optional remote trigger.
- **Snapshots**: single-tap capture with metadata (resolution, FPS, camera settings).

### Controls & Telemetry
- **Drive controls**: joystick + D-pad + speed slider.
- **Pan/tilt**: on-screen pad + gyro mode.
- **Telemetry panel**: RSSI, latency, FPS, CPU load, battery.
- **Command channel**: BLE or TCP/UDP to send control packets.

### Profiles
- **Profile manager**: save/load camera + stream settings (Indoor, Outdoor, Low‑latency).
- **Quick switch**: one-tap profile switch from the main screen.

---

## UX / Screen Map
1. **Home / Connection**
   - Connect via Wi‑Fi/BLE
   - Device discovery
   - Recent devices

2. **Drive & Stream (Main Screen)**
   - Live video view (full screen)
   - Joystick + pan/tilt overlay
   - Quick settings drawer (resolution, FPS, bitrate)
   - Record + snapshot buttons

3. **Camera Settings**
   - Manual controls: exposure, WB, focus, gain
   - Image profile presets
   - Camera capability list (query supported ranges)

4. **Profiles**
   - Create/edit profiles
   - Assign labels and icons

5. **Telemetry & Diagnostics**
   - Stream stats
   - Network status
   - System stats

---

## Architecture

### App Layers
- **UI**: Jetpack Compose + MVI (or MVVM)
- **Domain**: Use cases for stream control, camera control, and robot control
- **Data**:
  - Stream client (RTSP/WebRTC)
  - Control channel (BLE + TCP/UDP)
  - Settings store (DataStore)

### Streaming Options
- **RTSP** (simple, low effort)
- **WebRTC** (lowest latency, adaptive bitrate)
- **HTTP MJPEG** (fallback)

### Camera Control Protocol
- Send camera parameter updates to the Pi service:
  ```json
  {
    "camera": "usb0",
    "exposure": {"mode": "manual", "value": 12000},
    "white_balance": {"mode": "manual", "temperature": 5200},
    "focus": {"mode": "manual", "distance": 0.8},
    "fps": 30,
    "bitrate_kbps": 1800
  }
  ```

---

## API Contracts (Proposed)

### 1) Camera Capabilities
**GET** `/camera/capabilities`
```json
{
  "camera": "usb0",
  "exposure": {"min": 100, "max": 33000, "step": 100},
  "focus": {"min": 0.1, "max": 1.0, "step": 0.01},
  "fps": [10, 15, 30],
  "resolutions": ["640x480", "1280x720", "1920x1080"]
}
```

### 2) Apply Settings
**POST** `/camera/settings`
```json
{
  "camera": "usb0",
  "exposure": {"mode": "manual", "value": 12000},
  "white_balance": {"mode": "auto"},
  "fps": 30,
  "resolution": "1280x720",
  "bitrate_kbps": 2000
}
```

### 3) Stream Control
**POST** `/stream/start` and `/stream/stop`

---

## Suggested Implementation Plan
1. **Foundation**: new Android app skeleton + stream playback
2. **Control channel**: robot drive + pan/tilt
3. **Camera settings UI** + API integration
4. **Profiles & telemetry**
5. **Polish**: error handling, diagnostics, onboarding

---

## Open Questions
- What stream protocol does the Pi currently provide (RTSP/WebRTC/MJPEG)?
- Does the Pi expose a settings API today, or must we build one?
- Do we need multiple cameras (USB + CSI)?

