package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.objects.EnergyType
import com.adriav.tcgpokemon.objects.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(private val dao: CardDao) : ViewModel() {
    val collection = dao.getAllCards()

    private val _selectedEnergy = MutableStateFlow<EnergyType?>(null)
    val selectedEnergy = _selectedEnergy

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery
    private val _selectedSet = MutableStateFlow<String?>(null)
    val selectedSet = _selectedSet

    val filteredCollection = combine(collection, selectedEnergy, searchQuery, selectedSet)
    { cards, energyType, query, set ->
        val normalizedQuery = query.normalize()

        cards.filter { card ->
            val matchesEnergy = when {
                energyType == null -> true
                card.type == null -> false
                else -> card.type.equals(energyType.apiName, ignoreCase = true)
            }
            val matchesQuery = normalizedQuery.isBlank() || card.name.normalize()
                .contains(normalizedQuery, ignoreCase = true)

            val matchesSet = set == null || card.set == set

            matchesEnergy && matchesQuery && matchesSet
        }
    }

    fun selectEnergy(energyType: EnergyType?) {
        _selectedEnergy.value = energyType
    }

    fun onSearchQuery(query: String) {
        val normalizedQuery = query.normalize()
        _searchQuery.value = normalizedQuery
    }

    fun selectSet(setId: String?) {
        _selectedSet.value = setId
    }

}