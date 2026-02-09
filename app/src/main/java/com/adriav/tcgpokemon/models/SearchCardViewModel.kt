package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.database.entity.CardEntity
import com.adriav.tcgpokemon.objects.CardImageRepository
import com.adriav.tcgpokemon.objects.normalize
import com.adriav.tcgpokemon.views.search.SearchCardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.CardResume
import javax.inject.Inject

@HiltViewModel
class SearchCardViewModel @Inject constructor(
    private val tcgdex: TCGdex,
    private val dao: CardDao,
    private val cardImageRepository: CardImageRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchCardUiState>(SearchCardUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var allCards: List<CardResume> = emptyList()

    private val _selectedCards = MutableStateFlow<Set<String>>(emptySet())
    val selectedCards = _selectedCards.asStateFlow()
    val selectionMode: StateFlow<Boolean> =
        _selectedCards.map { it.isNotEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        fetchAllCards()
        observeSearch()
    }

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

    fun addSelectedToCollection(cards: List<CardResume>) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _selectedCards.value

            cards
                .filter { it.id in selected }
                .forEach { card ->
                    val fullCard = tcgdex.fetchCard(card.id)
                    if (fullCard != null) {
                        var imageURL = fullCard.image
                        imageURL = if (imageURL == null) {
                            cardImageRepository.getImage(card.id) ?: ""
                        } else {
                            fullCard.getImageUrl(Quality.HIGH, Extension.WEBP)
                        }
                        val cardEntity = CardEntity(
                            id = fullCard.id,
                            name = fullCard.name,
                            category = fullCard.category,
                            rarity = fullCard.rarity,
                            type = fullCard.types?.get(0),
                            set = fullCard.set.name,
                            imageUrl = imageURL
                        )
                        dao.insertCard(cardEntity)
                    }
                }
            clearSelection()
        }
    }

    private fun fetchAllCards() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    tcgdex.fetchCards()
                }
                allCards = result!!.asList()
                _uiState.value = SearchCardUiState.Idle
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SearchCardUiState.Error(
                    e.message ?: "Error retrieving cards"
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
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
        if (query.isBlank()) {
            _uiState.value = SearchCardUiState.Idle
            return
        }

        _uiState.value = SearchCardUiState.Loading
        val normalizedQuery = query.normalize()
        val filtered = allCards.filter { card ->
            card.name.normalize().contains(normalizedQuery, ignoreCase = true)
        }

        if (filtered.isEmpty()) {
            _uiState.value = SearchCardUiState.Error("No cards found")
            return
        }
        _uiState.value = SearchCardUiState.Success(filtered)
    }

    fun onQueryChange(query: String) {
        val normalizedQuery = query.normalize()
        _searchQuery.value = normalizedQuery
    }

    fun getCardImageRepository(): CardImageRepository {
        return cardImageRepository
    }
}