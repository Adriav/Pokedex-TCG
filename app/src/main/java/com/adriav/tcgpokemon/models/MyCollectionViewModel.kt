package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.objects.EnergyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(private val dao: CardDao) : ViewModel() {
    val collection = dao.getAllCards()

    private val _selectedEnergy = MutableStateFlow<EnergyType?>(null)
    val selectedEnergy = _selectedEnergy

    val filteredCollection = combine(collection, selectedEnergy) { cards, energyType ->
        if (energyType == null) {
            cards
        } else {
            cards.filter { card ->
                card.type!!.equals(energyType.apiName, ignoreCase = true)
            }
        }
    }

    fun selectEnergy(energyType: EnergyType?) {
        _selectedEnergy.value = energyType
    }

}