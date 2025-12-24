package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.objects.TCGdexProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.Card

@Composable
fun SingleCardView(cardID: String, paddingValues: PaddingValues) {
    val tcgdex = TCGdexProvider.tcgdex
    var card by remember { mutableStateOf<Card?>(null) }

    LaunchedEffect(Unit) {
        val api = tcgdex
        card = withContext(Dispatchers.IO) {
            api.fetchCard(cardID)
        }
    }

    if (card == null) {
        CenteredProgressIndicator()
    } else {
        val imageURL: String = card!!.getImageUrl(Quality.HIGH, Extension.WEBP)
        Column() {

            Text(text = "${card!!.name} - ${card!!.id}")
            AsyncImage(
                model = imageURL,
                contentDescription = card!!.name,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }
}