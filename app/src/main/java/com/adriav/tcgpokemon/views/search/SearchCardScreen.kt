package com.adriav.tcgpokemon.views.search

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val selected by viewModel.selectedCards.collectAsState()
    val selectionMode by viewModel.selectionMode.collectAsState()
    val context = LocalContext.current
    BackHandler(enabled = selectionMode) { viewModel.clearSelection() }

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
                Scaffold(
                    contentWindowInsets = WindowInsets.systemBars,
                    bottomBar = {
                        if (selectionMode) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .padding(vertical = 8.dp),
                                onClick = {
                                    val selectedCards = cards.filter { it.id in selected }
                                    viewModel.addSelectedToCollection(selectedCards)
                                    if (selectedCards.size > 1) Toast.makeText(
                                        context,
                                        "Cards added",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    else Toast.makeText(context, "Card added", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "ADD TO COLLECTION",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            /*
                            Surface(
                                tonalElevation = 3.dp,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp)
                                    .navigationBarsPadding()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
//                                        .height(64.dp)
                                        .padding(horizontal = 16.dp),
//                                        .navigationBarsPadding(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${selected.size} seleccionadas",
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Button(
                                        onClick = {
                                            viewModel.addSelectedToCollection(cards.filter { it.id in selected })
                                            Toast.makeText(context, "Cards added", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Text("Agregar")
                                    }
                                }
                            }
                            */
                            /*
                            BottomAppBar (
                                modifier = Modifier.height(75.dp)
                            ) {
                                Text(
                                    text = "${selected.size} selected",
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(16.dp)
                                )
                                Button(
                                    onClick = {
                                        viewModel.addSelectedToCollection(cards.filter { it.id in selected })
                                        Toast.makeText(context, "Cards added", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text("Add to Collection")
                                }
                            }

                             */
                        }

                    }
                ) { _ ->
                    DisplayCardGrid(
                        cards = cards,
                        viewModel = viewModel,
                        selected = selected,
                        selectionMode = selectionMode,
                        onCardClick = onCardClick
                    )
                }
            }

            is SearchCardUiState.Error -> DisplaySearchError(uiState)
        }
    }
}

@Composable
fun DisplayCardGrid(
    cards: List<CardResume>,
    viewModel: SearchCardViewModel,
    selected: Set<String>,
    selectionMode: Boolean,
    onCardClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(140.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards, key = { it.id }) { card ->
            Box {
                CardSearchItemView(
                    cardResume = card,
                    isSelected = card.id in selected,
                    selectionMode = selectionMode,
                    onClick = {
                        if (selectionMode) {
                            viewModel.onCardClick(card.id)
                        } else {
                            onCardClick(card.id)
                        }
                    },
                    onLongPress = {
                        viewModel.onCardLongPress(card.id)
                    }
                )
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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