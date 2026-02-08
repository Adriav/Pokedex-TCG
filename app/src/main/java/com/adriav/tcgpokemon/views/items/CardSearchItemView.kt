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
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.objects.getCardResumeImageURL
import net.tcgdex.sdk.models.CardResume

@Composable
fun CardSearchItemView(
    cardResume: CardResume,
    isSelected: Boolean,
    selectionMode: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            )
    ) {
        CardPreview(cardResume)
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
private fun CardPreview(cardResume: CardResume) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.65f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = cardResume.name, modifier = Modifier.padding(horizontal = 8.dp))
            AsyncImage(
                model = getCardResumeImageURL(cardResume),
                contentDescription = cardResume.name,
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.card_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}