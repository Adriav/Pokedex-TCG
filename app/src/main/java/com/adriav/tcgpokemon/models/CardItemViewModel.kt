package com.adriav.tcgpokemon.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.database.dao.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardItemViewModel @Inject constructor( private val dao: CardDao) : ViewModel() {
    private val _isCollected = MutableLiveData<Boolean>()
    val isCollected: LiveData<Boolean> = _isCollected

    fun getIsCollected(cardID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = dao.cardExists(cardID)
                _isCollected.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}