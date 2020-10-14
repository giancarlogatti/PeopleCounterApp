package com.example.peoplecounter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CounterViewModel(private val savedStateHandle: SavedStateHandle): ViewModel(){

    companion object {
        const val TOTAL_COUNT_KEY = "total_count"
        const val TOTAL_PEOPLE_KEY = "total_people"
    }

    //retrieve state from handle, default to 0
    var totalCount = savedStateHandle.get(TOTAL_COUNT_KEY) ?: 0
    var totalPeople = savedStateHandle.get(TOTAL_PEOPLE_KEY) ?: 0

    fun saveState(totalC: Int, totalP: Int){
        totalCount = totalC
        totalPeople = totalP
        savedStateHandle.set(TOTAL_COUNT_KEY, totalCount)
        savedStateHandle.set(TOTAL_PEOPLE_KEY, totalPeople)
    }
}