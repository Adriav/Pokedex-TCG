package com.adriav.tcgpokemon.views.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.models.SearchCardViewModel
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.CardSearchItemView
import net.tcgdex.sdk.models.CardResume

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCardScreen(
    viewModel: SearchCardViewModel,
    onCardClick: (String) -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column {
        SearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text(text = "Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) { }

        when (uiState) {
            is SearchCardUiState.Idle -> EmptySearchHint()
            is SearchCardUiState.Loading -> CenteredProgressIndicator()

            is SearchCardUiState.Success -> {
                val cards = (uiState as SearchCardUiState.Success).cards
                DisplayCardGrid(cards = cards, onCardClick = onCardClick)
            }
            is SearchCardUiState.Error -> DisplaySearchError(uiState)
        }
    }
}

@Composable
fun DisplayCardGrid(cards: List<CardResume>, onCardClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(140.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards, key = { it.id }) { card ->
            Box(
                modifier = Modifier
                    .clickable { onCardClick(card.id) }
            ) {
                CardSearchItemView(cardResume = card)
            }
        }
    }
}

@Composable
fun EmptySearchHint() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Start typing",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DisplaySearchError(
    uiState: SearchCardUiState =
        SearchCardUiState.Error("No cards found")
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(R.drawable.verror_code_vector_icon),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = (uiState as SearchCardUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}