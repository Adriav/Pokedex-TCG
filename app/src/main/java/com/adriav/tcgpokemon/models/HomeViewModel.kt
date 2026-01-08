package com.adriav.tcgpokemon.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    private val _searchEnabled = MutableLiveData<Boolean>()
    val searchEnabled: LiveData<Boolean> = _searchEnabled

    fun onSearchChange(searchText: String) {
        _searchText.value = searchText
        _searchEnabled.value = searchText.isNotEmpty()
    }

}