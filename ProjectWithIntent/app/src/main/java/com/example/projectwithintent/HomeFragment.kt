package com.example.projectwithintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonTask1: Button = view.findViewById(R.id.button_task1)
        val buttonTask2: Button = view.findViewById(R.id.button_task2)
        val buttonTask3: Button = view.findViewById(R.id.button_task3)
        val buttonTask4: Button = view.findViewById(R.id.button_task4)

        buttonTask1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_task1Fragment)
        }

        buttonTask2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_task2Fragment)
        }

        buttonTask3.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_task3Fragment)
        }

        buttonTask4.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_task4Fragment)
        }
    }
}