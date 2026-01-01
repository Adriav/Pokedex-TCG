package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.objects.TCGdexProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.Card

@Composable
fun SingleCardScreen(cardID: String) {
    val tcgdex = TCGdexProvider.provideTCGdex()
    var card by remember { mutableStateOf<Card?>(null) }

    LaunchedEffect(Unit) {
        card = withContext(Dispatchers.IO) {
            tcgdex.fetchCard(cardID)
        }
    }

    if (card == null) {
        CenteredProgressIndicator()
    } else {
        val imageURL: String = card!!.getImageUrl(Quality.HIGH, Extension.WEBP)
        Column {
            AppHeader(card!!.name)
            AsyncImage(
                model = imageURL,
                contentDescription = card!!.name,
                modifier = Modifier.fillMaxWidth()
                    .padding(24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}