package com.example.frontend_mobile.data

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

object WebSocketManager {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var listener: ((tipo: String, evento: String, id: String) -> Unit)? = null

    fun conectar(onEvento: (tipo: String, evento: String, id: String) -> Unit) {
        listener = onEvento
        val request = Request.Builder().url("ws://10.0.2.2:8080/ws").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val tipo = json.optString("tipo", "desconocido")
                    val evento = json.optString("evento", "desconocido")
                    val id = json.optString("id", "")
                    listener?.invoke(tipo, evento, id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun cerrar() {
        webSocket?.close(1000, null)
    }
}