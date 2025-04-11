package com.example.chatlibrary.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlibrary.R
import com.example.chatlibrary.model.ChatMessage
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: ChatAdapter

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recycler_chat)
        editText = findViewById(R.id.edit_message)
        sendButton = findViewById(R.id.button_send)

        adapter = ChatAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        connectWebSocket()

        sendButton.setOnClickListener {
            val message = editText.text.toString()
            if (message.isNotBlank()) {
                sendMessage(message)
                editText.text.clear()
            }
        }
    }

    private fun connectWebSocket() {
        val request = Request.Builder()
            .url("wss://echo.websocket.org")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d("WebSocket", "Received: $text")
                runOnUiThread {
                    val displayMessage = if (text == "203 = 0xcb") {
                        " Получено системное сообщение!"
                    } else {
                        text
                    }
                    adapter.addMessage(ChatMessage(displayMessage, isSentByUser = false))
                }
            }

            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                val hexString = bytes.hex()
                Log.d("WebSocket", "Binary received: $hexString")
                runOnUiThread {
                    val displayMessage = if (hexString == "cb") {
                        " Получено бинарное сообщение 0xcb"
                    } else {
                        " Получен бинарный пакет: $hexString"
                    }
                    adapter.addMessage(ChatMessage(displayMessage, isSentByUser = false))
                }
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}", t)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing: $code / $reason")
                ws.close(1000, null)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closed: $code / $reason")
            }
        })
    }

    private fun sendMessage(message: String) {
        adapter.addMessage(ChatMessage(message, isSentByUser = true))
        webSocket?.send(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Activity Closed")
        client.dispatcher.executorService.shutdown()
    }
}