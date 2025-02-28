package com.example.projectwithintent

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectwithintent.Services.CalendarEvent
import com.example.projectwithintent.Services.CalendarEventsAdapter

class Task4Fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarEventsAdapter
    private val events = mutableListOf<CalendarEvent>()

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CALENDAR = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_task4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_calendar_events)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CalendarEventsAdapter(events)
        recyclerView.adapter = adapter

        checkPermission(PERMISSIONS_REQUEST_READ_CALENDAR, android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR)
    }

    private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
        var permissionsGranted = true
        for (permission in permissionsId) {
            permissionsGranted = permissionsGranted && ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }

        if (!permissionsGranted) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsId, callbackId)
        } else {
            fetchCalendarEvents()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CALENDAR) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                fetchCalendarEvents()
            }
        }
    }

    private fun fetchCalendarEvents() {
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )

        val cursor: Cursor? = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null,
            null,
            "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startTimeIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val startTime = it.getLong(startTimeIndex)
                events.add(CalendarEvent(title, startTime))
            }
        }

        adapter.notifyDataSetChanged()
    }
}