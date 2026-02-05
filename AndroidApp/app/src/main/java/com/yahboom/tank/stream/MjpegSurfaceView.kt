package com.yahboom.tank.stream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

class MjpegSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val running = AtomicBoolean(false)
    private var streamThread: Thread? = null
    private var streamUrl: String? = null

    init {
        holder.addCallback(this)
    }

    fun startStream(url: String) {
        streamUrl = url
        if (running.get()) return
        running.set(true)
        streamThread = Thread { streamLoop() }.also { it.start() }
    }

    fun stopStream() {
        running.set(false)
        streamThread?.interrupt()
        streamThread = null
    }

    private fun streamLoop() {
        val url = streamUrl ?: return
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                doInput = true
                useCaches = false
                setRequestProperty("Connection", "Keep-Alive")
            }
            connection.connect()

            BufferedInputStream(connection.inputStream, 8192).use { input ->
                while (running.get()) {
                    val jpegBytes = readJpegFrame(input) ?: break
                    val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size) ?: continue
                    drawFrame(bitmap)
                }
            }
        } catch (_: Exception) {
        } finally {
            connection?.disconnect()
            running.set(false)
        }
    }

    private fun readJpegFrame(input: BufferedInputStream): ByteArray? {
        val header = readHeader(input) ?: return null
        val contentLength = parseContentLength(header)
        return if (contentLength != null && contentLength > 0) {
            readExactBytes(input, contentLength)
        } else {
            readUntilJpegEnd(input)
        }
    }

    private fun readHeader(input: BufferedInputStream): String? {
        val headerBuffer = ByteArrayOutputStream()
        var prev = 0
        var curr: Int
        while (true) {
            curr = input.read()
            if (curr == -1) return null
            headerBuffer.write(curr)
            if (prev == '\r'.code && curr == '\n'.code) {
                val headerString = headerBuffer.toString(Charsets.US_ASCII)
                if (headerString.endsWith("\r\n\r\n")) {
                    return headerString
                }
            }
            prev = curr
        }
    }

    private fun parseContentLength(header: String): Int? {
        val match = Regex("Content-Length: (\\d+)", RegexOption.IGNORE_CASE).find(header)
        return match?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun readExactBytes(input: BufferedInputStream, length: Int): ByteArray? {
        val buffer = ByteArray(length)
        var totalRead = 0
        while (totalRead < length) {
            val read = input.read(buffer, totalRead, length - totalRead)
            if (read == -1) return null
            totalRead += read
        }
        return buffer
    }

    private fun readUntilJpegEnd(input: BufferedInputStream): ByteArray? {
        val buffer = ByteArrayOutputStream()
        var prev = 0
        var curr: Int
        while (true) {
            curr = input.read()
            if (curr == -1) return null
            buffer.write(curr)
            if (prev == 0xFF && curr == 0xD9) {
                break
            }
            prev = curr
        }
        return buffer.toByteArray()
    }

    private fun drawFrame(bitmap: Bitmap) {
        val canvas: Canvas = holder.lockCanvas() ?: return
        canvas.drawColor(Color.BLACK)
        val scale = minOf(
            width.toFloat() / bitmap.width,
            height.toFloat() / bitmap.height
        )
        val drawWidth = bitmap.width * scale
        val drawHeight = bitmap.height * scale
        val left = (width - drawWidth) / 2f
        val top = (height - drawHeight) / 2f
        canvas.drawBitmap(bitmap, null, RectF(left, top, left + drawWidth, top + drawHeight), null)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(holder: SurfaceHolder) = Unit
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit
    override fun surfaceDestroyed(holder: SurfaceHolder) { stopStream() }
}
