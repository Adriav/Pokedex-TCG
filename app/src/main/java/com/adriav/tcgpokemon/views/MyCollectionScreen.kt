package com.adriav.tcgpokemon.views

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.models.MyCollectionViewModel
import com.adriav.tcgpokemon.objects.EnergyIcon
import com.adriav.tcgpokemon.objects.EnergyType
import com.adriav.tcgpokemon.views.items.CollectionCardItem

@Composable
fun MyCollectionScreen(
    viewModel: MyCollectionViewModel,
    onCardClick: (String) -> Unit
) {
    val cards by viewModel.filteredCollection
        .collectAsState(initial = emptyList())

    val selectedEnergy by viewModel.selectedEnergy
        .collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()


    Column {
        CollectionSearchBar(query = searchQuery, onQueryChange = viewModel::onSearchQuery)
        EnergyFilterRow(selectedEnergy, viewModel::selectEnergy)
        Spacer(modifier = Modifier.height(8.dp))
        if (cards.isEmpty()) {
            ShowEmptyCollection()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = cards,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionSearchBar(query: String, onQueryChange: (String) -> Unit) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        placeholder = { Text("Search") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") }
    ) { }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEmptyCollection() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(width = 200.dp, height = 200.dp),
                    color = Color(0xFF2D6BE0),
                    strokeWidth = 12.dp
                )
            }
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

@Composable
fun EnergyFilterRow(selectedEnergy: EnergyType?, onEnergySelected: (EnergyType?) -> Unit) {
    val energies = listOf(
        EnergyType.Colorless,
        EnergyType.Darkness,
        EnergyType.Fairy,
        EnergyType.Water,
        EnergyType.Fire,
        EnergyType.Grass,
        EnergyType.Lightning,
        EnergyType.Metal,
        EnergyType.Psychic,
        EnergyType.Fighting,
        EnergyType.Dragon
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterChip(
            selected = selectedEnergy == null,
            onClick = { onEnergySelected(null) },
            label = { Text(text = "All") }
        )

        energies.forEach { energy ->
            FilterChip(
                selected = selectedEnergy == energy,
                onClick = { onEnergySelected(energy) },
                leadingIcon = {
                    EnergyIcon(energyType = energy.apiName, modifier = Modifier.height(20.dp))
                },
                label = { Text(text = energy.apiName) }
            )
        }
    }
}