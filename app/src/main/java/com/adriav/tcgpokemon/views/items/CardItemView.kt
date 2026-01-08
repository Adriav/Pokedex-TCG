package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.CardResume

@Composable
fun CardItemView(cardResume: CardResume, index: Int? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Row (
                    modifier = Modifier
                        .width(300.dp)
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (index != null) {
                        Text(text = "$index - ${cardResume.name}", fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = cardResume.name, fontWeight = FontWeight.Bold)
                    }
                }
                val cardImage =
                    cardResume.getImageUrl(Quality.LOW, Extension.WEBP)
                        .replace("LOW", "low")
                AsyncImage(
                    model = cardImage,
                    contentDescription = cardResume.id,
                    modifier = Modifier
                        .width(300.dp)
                        .height(400.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.loading_progress_icon),
                    error = painterResource(R.drawable.verror_code_vector_icon)
                )
            }
        }
    }
}