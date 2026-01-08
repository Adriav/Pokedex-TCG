package com.adriav.tcgpokemon.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.SerieResume
import javax.inject.Inject

@HiltViewModel
class AllSeriesViewModel @Inject constructor(private val tcgdex: TCGdex) : ViewModel() {
    private val _cardSeries = MutableLiveData<Array<SerieResume>>()
    val cardSeries: LiveData<Array<SerieResume>> = _cardSeries

    fun loadAllSeries() {
        // Use viewModelScope. This automatically cancels the request if the ViewModel is cleared.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Perform network call on IO thread
                val response = tcgdex.fetchSeries()

                // 2. Update LiveData
                // postValue safely updates LiveData from a background thread to the Main thread
                _cardSeries.postValue(response!!)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}