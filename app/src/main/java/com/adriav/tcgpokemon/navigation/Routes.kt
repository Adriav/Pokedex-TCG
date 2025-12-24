package com.adriav.tcgpokemon.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes: NavKey {
    @Serializable
    data object Home:Routes()

    @Serializable
    data class SingleCard(val id: String): Routes()

    @Serializable
    data object AllSets: Routes()

    @Serializable
    data object AllSeries: Routes()

    @Serializable
    data class SingleSerie(val id: String): Routes()

    @Serializable
    data class SingleSet(val id: String): Routes()

    @Serializable
    data object MyCards: Routes()

    @Serializable
    data class CardSearchResult(val query: String): Routes()

    @Serializable
    data object Error: Routes()
}