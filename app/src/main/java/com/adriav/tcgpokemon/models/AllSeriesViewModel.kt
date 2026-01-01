package com.adriav.tcgpokemon.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.objects.TCGdexProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.models.SerieResume
import javax.inject.Inject

class AllSeriesViewModel @Inject constructor(private val tcgdex: TCGdexProvider = TCGdexProvider) : ViewModel() {
    private val _cardSeries = MutableLiveData<Array<SerieResume>>()
    val cardSeries: LiveData<Array<SerieResume>> = _cardSeries

    fun loadAllSeries() {
        // Use viewModelScope. This automatically cancels the request if the ViewModel is cleared.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Perform network call on IO thread
                val response = tcgdex.provideTCGdex().fetchSeries()

                // 2. Update LiveData
                // postValue safely updates LiveData from a background thread to the Main thread
                _cardSeries.postValue(response!!)

            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error state here if needed
            }
        }

        // DELETED: _cardSeries.value = tcgdex.fetchSeries()
        // Reason: This line was running on the Main Thread and causing your app to crash.
    }
}