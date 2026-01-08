package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.clickable
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
import com.adriav.tcgpokemon.database.entity.CardEntity

@Composable
fun CollectionCardItem(card: CardEntity, onClick: (CardEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.65f)
            .clickable { onClick(card) },
        elevation = CardDefaults.cardElevation((4.dp))
    ) {
        val imageURL = card.imageUrl.replace("HIGH","LOW")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = card.name)
            AsyncImage(
                model = imageURL,
                contentDescription = card.name,
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.verror_code_vector_icon),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = card.addedAt.toString())
        }
    }
}