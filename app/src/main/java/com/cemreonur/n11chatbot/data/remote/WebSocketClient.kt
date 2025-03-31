package com.cemreonur.n11chatbot.data.remote

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient(private val url: String) {

    private val client = OkHttpClient()
    private val _messages = MutableSharedFlow<String>()
    val messages: Flow<String> = _messages

    private val _currentStep = MutableSharedFlow<String>()
    val currentStep: Flow<String> = _currentStep

    private var webSocket: WebSocket? = null

    var onMessageReceived: ((String) -> Unit)? = null

    fun connect() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "✅ WebSocket Connection Opened!")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "❌ WebSocket Error: ${t.message}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "📩 Receiving Message: $text")
                CoroutineScope(Dispatchers.IO).launch {
                    _messages.emit(text)
                }
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun closeConnection() {
        webSocket?.close(1000, "Conversation ended")
        webSocket = null
        Log.d("WebSocketClient", "WebSocket bağlantısı kapatıldı.")
    }

    fun observeMessages(): Flow<String> = messages
}
