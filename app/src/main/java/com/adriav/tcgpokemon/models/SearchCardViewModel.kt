package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.objects.normalize
import com.adriav.tcgpokemon.views.search.SearchCardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.CardResume
import javax.inject.Inject

@HiltViewModel
class SearchCardViewModel @Inject constructor(private val tcgdex: TCGdex) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchCardUiState>(SearchCardUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var allCards: List<CardResume> = emptyList()

    init {
        fetchAllCards()
        observeSearch()
    }

    private fun fetchAllCards() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    tcgdex.fetchCards()
                }
                allCards = result!!.asList()
                _uiState.value = SearchCardUiState.Loading
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SearchCardUiState.Error(
                    e.message ?: "Error retrieving cards"
                )
            }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .filter { it.length >= 2 }
                .collectLatest { query ->
                    filterCards(query)
                }
        }
    }

    private fun filterCards(query: String) {
        if(query.isBlank()) {
            _uiState.value = SearchCardUiState.Idle
            return
        }

        _uiState.value = SearchCardUiState.Loading
        val normalizedQuery = query.normalize()
        val filtered = allCards.filter {card ->
            card.name.normalize().contains(normalizedQuery, ignoreCase = true)
        }

        _uiState.value = SearchCardUiState.Success(filtered)
    }

    fun onQueryChange(query: String) {
        val normalizedQuery = query.normalize()
        _searchQuery.value = normalizedQuery
    }
}