package com.example.backend.webconfig

import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArraySet
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Component
class SocketHandler : TextWebSocketHandler() {

    private val sessions = CopyOnWriteArraySet<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
        sessions.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // Opcional: manejar mensajes entrantes del cliente si se desea
    }

    /**
     * Envía un mensaje a todos los clientes conectados.
     * @param tipo Tipo de entidad afectada (ej. curso, usuario, orden).
     * @param evento Evento realizado (ej. insertar, eliminar, actualizar).
     * @param id Identificador principal de la entidad afectada (ej. código o UUID).
     */
    fun notificarCambio(tipo: String, evento: String, id: String) {
        val mensajeJson = Json.encodeToString(
            buildJsonObject {
                put("tipo", tipo)
                put("evento", evento)
                put("id", id)
            }
        )
        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(TextMessage(mensajeJson))
            }
        }
    }
}