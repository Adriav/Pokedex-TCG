package com.adriav.tcgpokemon.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.database.entity.CardEntity
import com.adriav.tcgpokemon.models.MyCollectionViewModel
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.CollectionCardItem

@Composable
fun MyCollectionScreen(
    viewModel: MyCollectionViewModel,
    onCardClick: (String) -> Unit
) {
    // Cards as CollectAsState
    val cards by viewModel.collection.collectAsState(null)

    if (cards == null) {
        CenteredProgressIndicator()
    } else if (cards!!.isEmpty()) {
        ShowEmptyCollection()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = cards as List<CardEntity>,
                key = { it.id }
            ) { card ->
                CollectionCardItem(
                    card = card,
                    onClick = {
                        onCardClick(card.id)
                    }
                )
            }
        }
    }
}

@Composable
fun ShowEmptyCollection() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Image(
                painter = painterResource(R.drawable.sad_bulbasaur),
                contentDescription = "sad",
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "NO CARDS YET...",
                fontSize = 40.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}