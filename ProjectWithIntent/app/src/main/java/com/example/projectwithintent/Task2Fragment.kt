package com.example.projectwithintent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.projectwithintent.Services.MusicService

class Task2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonStart: Button = view.findViewById(R.id.button_start)
        val buttonPause: Button = view.findViewById(R.id.button_pause)
        val buttonStop: Button = view.findViewById(R.id.button_stop)

        buttonStart.setOnClickListener {
            val intent = Intent(activity, MusicService::class.java).apply {
                action = MusicService.ACTION_START
            }
            activity?.startService(intent)
        }

        buttonPause.setOnClickListener {
            val intent = Intent(activity, MusicService::class.java).apply {
                action = MusicService.ACTION_PAUSE
            }
            activity?.startService(intent)
        }

        buttonStop.setOnClickListener {
            val intent = Intent(activity, MusicService::class.java).apply {
                action = MusicService.ACTION_STOP
            }
            activity?.startService(intent)
        }
    }
}