package com.example.peoplecounter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.peoplecounter.databinding.ActivityCounterBinding
import kotlin.properties.Delegates

@SuppressLint("SetTextI18n")
class CounterActivity : AppCompatActivity() {

    companion object{
        //when this number is exceeded, the text that reflects this value changes from blue to red
        const val MAX_THRESHOLD = 15
        const val TOTAL_COUNT_KEY = "total_count"
        const val TOTAL_PEOPLE_KEY = "total_people"
    }

    private lateinit var binding: ActivityCounterBinding
    private lateinit var sharedPref: SharedPreferences

    private var totalPeople by Delegates.observable(0){ _, _, newCount ->
        binding.tvPeopleCounter.text = "$newCount People"
        if(newCount > MAX_THRESHOLD){
            binding.tvPeopleCounter.setTextColor(Color.RED)
        } else {
            binding.tvPeopleCounter.setTextColor(Color.BLUE)
        }
        if(newCount >= 1){ //button to reduce the total people count needs to be visible
            binding.btnReduce.visibility = View.VISIBLE
        } else {
            binding.btnReduce.visibility = View.INVISIBLE
        }
        sharedPref.edit().putInt(TOTAL_PEOPLE_KEY, newCount).apply()
    }

    private var totalCount by Delegates.observable(0) { _, _, newCount ->
        binding.tvTotalCounter.text = "Total Count: $newCount"
        sharedPref.edit().putInt(TOTAL_COUNT_KEY, newCount).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        totalCount = sharedPref.getInt(TOTAL_COUNT_KEY, 0)
        totalPeople = sharedPref.getInt(TOTAL_PEOPLE_KEY, 0)

        binding.btnReset.setOnClickListener {
            totalCount = 0
            totalPeople = 0
        }

        binding.btnAdd.setOnClickListener {
            totalCount += 1
            totalPeople += 1
        }

        binding.btnReduce.setOnClickListener {
            if(totalPeople != 0){
                totalPeople -= 1
            }
        }
    }
}