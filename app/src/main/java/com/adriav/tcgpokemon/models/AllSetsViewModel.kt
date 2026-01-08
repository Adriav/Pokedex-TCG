package com.adriav.tcgpokemon.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.SetResume
import javax.inject.Inject

@HiltViewModel
class AllSetsViewModel @Inject constructor(private val tcgdex: TCGdex) : ViewModel() {
    private val _cardSets = MutableLiveData<Array<SetResume>>()
    val cardSets = _cardSets
    fun loadAllSets() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tcgdex.fetchSets()
                _cardSets.postValue(response!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}