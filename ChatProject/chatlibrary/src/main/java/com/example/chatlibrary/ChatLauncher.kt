package com.example.chatlibrary

import android.content.Context
import android.content.Intent
import com.example.chatlibrary.ui.ChatActivity

object ChatLauncher {
    fun start(context: Context) {
        val intent = Intent(context, ChatActivity::class.java)
        context.startActivity(intent)
    }
}