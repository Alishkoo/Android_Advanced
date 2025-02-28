package com.example.projectwithintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projectwithintent.Services.AirplaneModeReceiver

class Task3Fragment : Fragment() {

    private lateinit var airplaneModeReceiver: BroadcastReceiver
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_task3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView = view.findViewById(R.id.textView_airplane_mode_status)

        airplaneModeReceiver = AirplaneModeReceiver { isAirplaneModeOn ->
            updateUI(isAirplaneModeOn)
        }
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireActivity().registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(airplaneModeReceiver)
    }

    private fun updateUI(isAirplaneModeOn: Boolean) {
        textView.text = if (isAirplaneModeOn) {
            "Airplane Mode is ON"
        } else {
            "Airplane Mode is OFF"
        }
    }
}