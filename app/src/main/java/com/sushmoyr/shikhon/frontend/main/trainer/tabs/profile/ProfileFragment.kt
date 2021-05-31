package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sushmoyr.shikhon.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val date = LocalDate.now()
        val time = LocalTime.now()
        val dateString = date.toString()
        val timeString = time.toString()
        Log.d("Time", "Creation Date: $dateString")
        Log.d("Time", "Creation Moment: $timeString")
        val parsedDate = LocalDate.parse(dateString)
        val parsedTime = LocalTime.parse(timeString)

        Log.d("Time","Parse at ${parsedDate.toString()}")
        Log.d("Time","Parsed at $parsedTime}")

        val dateTime = LocalDateTime.now()

        val dateTimeString = dateTime.toString()
        Log.d("TIme", dateTime.toString())
        val updateDateTime = LocalDateTime.parse(dateTimeString)
        Log.d("Time","${updateDateTime.dayOfWeek}")
        Log.d("Time","${updateDateTime.month}")
        Log.d("Time","${updateDateTime.year}")
        Log.d("Time","${updateDateTime.hour}")
        Log.d("Time","${updateDateTime.minute}")


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}