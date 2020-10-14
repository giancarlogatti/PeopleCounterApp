package com.example.peoplecounter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.peoplecounter.databinding.ActivityCounterBinding
import kotlin.properties.Delegates

@SuppressLint("SetTextI18n")
class CounterActivity : AppCompatActivity() {

    companion object{
        //when this number is exceeded, the text that reflects this value changes from blue to red
        const val PEOPLE_COUNT_THRESHOLD = 15
    }

    private lateinit var binding: ActivityCounterBinding
    private val counterViewModel: CounterViewModel by viewModels()

    private var totalPeople by Delegates.observable(0){ _, _, newCount ->
        binding.tvPeopleCounter.text = "$newCount People"
        if(newCount > PEOPLE_COUNT_THRESHOLD){
            binding.tvPeopleCounter.setTextColor(Color.RED)
        } else {
            binding.tvPeopleCounter.setTextColor(Color.BLUE)
        }
        if(newCount >= 1){ //button to reduce the total people count needs to be visible
            binding.btnReduce.visibility = View.VISIBLE
        } else {
            binding.btnReduce.visibility = View.INVISIBLE
        }
    }

    private var totalCount by Delegates.observable(0) { _, _, newCount ->
        binding.tvTotalCounter.text = "Total Count: $newCount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        totalPeople = counterViewModel.totalPeople
        totalCount = counterViewModel.totalCount

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

    override fun onDestroy() {
        counterViewModel.saveState(totalCount, totalPeople)
        super.onDestroy()
    }
}