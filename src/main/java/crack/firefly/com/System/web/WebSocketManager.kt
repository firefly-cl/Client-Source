package crack.firefly.com.System.web.websocket

import crack.firefly.com.Console.FireflyConsole
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketManager {

    private var client: WebSocketClient? = null
    private var connected = false

    fun init() {
        FireflyConsole.info("Inicializando WebSocketManager")
        connect()
    }

    private fun connect() {
        try {
            val uri = URI("ws://localhost:8080")

            client = object : WebSocketClient(uri) {

                override fun onOpen(handshakedata: ServerHandshake) {
                    connected = true
                    FireflyConsole.info("WebSocket conectado com sucesso")

                    // mensagem de teste
                    send("HELLO_FROM_FIREFLY")
                }

                override fun onMessage(message: String) {
                    FireflyConsole.info("Mensagem recebida do WS: $message")
                }

                override fun onClose(code: Int, reason: String, remote: Boolean) {
                    connected = false
                    FireflyConsole.warn(
                        "WebSocket desconectado | code=$code reason=$reason remote=$remote"
                    )
                }

                override fun onError(ex: Exception) {
                    FireflyConsole.error("Erro no WebSocket", ex)
                }
            }

            FireflyConsole.info("Conectando ao WebSocket...")
            client!!.connect()

        } catch (e: Exception) {
            FireflyConsole.error("Falha ao iniciar WebSocket", e)
        }
    }

    fun sendMessage(message: String) {
        if (connected && client != null && client!!.isOpen) {
            client!!.send(message)
        } else {
            FireflyConsole.warn("Tentou enviar mensagem, mas o WebSocket não está conectado")
        }
    }

    fun shutdown() {
        try {
            if (client != null) {
                FireflyConsole.info("Fechando WebSocket")
                client!!.close()
            }
        } catch (e: Exception) {
            FireflyConsole.error("Erro ao fechar WebSocket", e)
        }
    }

    fun isConnected(): Boolean {
        return connected
    }
}
