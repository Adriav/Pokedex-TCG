package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.CardResume

@Composable
fun CardSearchItemView(cardResume: CardResume) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.65f),
        elevation = CardDefaults.cardElevation((4.dp))
    ) {
        val cardImage =
            cardResume.getImageUrl(Quality.LOW, Extension.WEBP)
                .replace("LOW", "low")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = cardResume.name)
            AsyncImage(
                model = cardImage,
                contentDescription = cardResume.name,
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.verror_code_vector_icon),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}