package com.adriav.tcgpokemon.views.search

import net.tcgdex.sdk.models.CardResume

sealed class SearchCardUiState {
    object Idle: SearchCardUiState()
    object Loading: SearchCardUiState()
    data class Success(val cards: List<CardResume>) : SearchCardUiState()
    data class Error(val message: String) : SearchCardUiState()
}