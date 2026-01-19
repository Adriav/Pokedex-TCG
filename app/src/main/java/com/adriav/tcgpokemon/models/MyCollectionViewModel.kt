package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.objects.CardFilter
import com.adriav.tcgpokemon.objects.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(dao: CardDao) : ViewModel() {
    val collection = dao.getAllCards()

    private val _selectedFilter = MutableStateFlow<CardFilter?>(null)
    val selectedFilter = _selectedFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery
    private val _selectedSet = MutableStateFlow<String?>(null)
    val selectedSet = _selectedSet

    val filteredCollection = combine(collection, selectedFilter, searchQuery, selectedSet)
    { cards, filter, query, set ->
        val normalizedQuery = query.normalize()

        cards.filter { card ->
            val matchesFilter = when (filter) {
                null -> true
                is CardFilter.Energy ->
                    card.type?.equals(
                        filter.energyType.apiName,
                        ignoreCase = true
                    ) == true && card.type != null

                is CardFilter.Category ->
                    card.category.equals(filter.category.name, ignoreCase = true)
            }

            val matchesQuery = normalizedQuery.isBlank() || card.name.normalize()
                .contains(normalizedQuery, ignoreCase = true)

            val matchesSet = set == null || card.set == set

            // matchesEnergy && matchesQuery && matchesSet
            matchesFilter && matchesQuery && matchesSet
        }
    }

    fun setFilter(filter: CardFilter?) {
        _selectedFilter.value = filter
    }

    fun onSearchQuery(query: String) {
        val normalizedQuery = query.normalize()
        _searchQuery.value = normalizedQuery
    }

    fun selectSet(setId: String?) {
        _selectedSet.value = setId
    }

}