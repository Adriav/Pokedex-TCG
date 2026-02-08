package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.objects.CardFilter
import com.adriav.tcgpokemon.objects.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(private val dao: CardDao) : ViewModel() {
    val collection = dao.getAllCards()

    private val _selectedFilter = MutableStateFlow<CardFilter?>(null)
    val selectedFilter = _selectedFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery
    private val _selectedSet = MutableStateFlow<String?>(null)
    val selectedSet = _selectedSet
    private val _selectedCards = MutableStateFlow<Set<String>>(emptySet())
    val selectedCards = _selectedCards.asStateFlow()

    val selectionMode: StateFlow<Boolean> =
        _selectedCards.map { it.isNotEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onCardLongPress(cardId: String) {
        toggleSelection(cardId)
    }

    fun onCardClick(cardId: String) {
        if (_selectedCards.value.isNotEmpty()) toggleSelection(cardId)
    }

    private fun toggleSelection(cardId: String) {
        val current = _selectedCards.value.toMutableSet()
        if (current.contains(cardId)) {
            current.remove(cardId)
        } else {
            current.add(cardId)
        }
        _selectedCards.value = current
    }

    fun clearSelection() {
        _selectedCards.value = emptySet()
    }

    fun removeSelectedFromCollection(cards: Set<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            cards.forEach { cardId ->
                dao.deleteCardById(cardId)
            }
            clearSelection()
        }
    }
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