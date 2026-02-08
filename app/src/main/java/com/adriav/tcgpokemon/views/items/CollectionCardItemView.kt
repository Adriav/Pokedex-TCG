package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.database.entity.CardEntity
import com.adriav.tcgpokemon.objects.toReadableDate

@Composable
fun CollectionCardItem(
    card: CardEntity,
    isSelected: Boolean,
    selectionMode: Boolean,
    onClick: (String) -> Unit,
    onLongPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = { onClick(card.id) },
                onLongClick = onLongPress
            )
    ) {
        CardPreview(card)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2086FF),
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }

}

@Composable
fun CardPreview(card: CardEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.65f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val imageURL = card.imageUrl.replace("HIGH", "LOW")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = card.name, modifier = Modifier.padding(horizontal = 4.dp), fontSize = 14.sp)
            AsyncImage(
                model = imageURL,
                contentDescription = card.name,
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.card_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = card.addedAt.toReadableDate(), style = MaterialTheme.typography.bodySmall)
        }
    }
}