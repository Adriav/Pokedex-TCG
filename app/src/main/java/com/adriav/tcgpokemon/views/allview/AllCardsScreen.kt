package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adriav.tcgpokemon.objects.TCGdexProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.models.CardResume

@Composable
fun AllCardsScreen() {
    val tcgdex = TCGdexProvider.provideTCGdex()
    var allCards: Array<CardResume>? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        allCards = withContext(Dispatchers.IO) {
            tcgdex.fetchCards()
        }
    }

    if (allCards == null) {
        CircularProgressIndicator()
    } else {
        var lotadCount = 0
        allCards!!.forEach { cardResume ->
            if (cardResume.name.contains("Lotad", ignoreCase = true)) {
                lotadCount++
            }
        }
        Column {
            Text(text = "Total de cartas: ${allCards!!.size}")
            Text(text = "Cartas de Lotad: $lotadCount")
        }
    }
}