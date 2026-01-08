package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import com.adriav.tcgpokemon.database.dao.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(private val dao: CardDao) : ViewModel() {
    val collection = dao.getAllCards()

}