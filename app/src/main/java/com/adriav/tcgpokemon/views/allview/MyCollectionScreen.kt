package com.adriav.tcgpokemon.views.allview

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.models.MyCollectionViewModel
import com.adriav.tcgpokemon.objects.CardCategory
import com.adriav.tcgpokemon.objects.CardFilter
import com.adriav.tcgpokemon.objects.EnergyIcon
import com.adriav.tcgpokemon.objects.EnergyType
import com.adriav.tcgpokemon.objects.normalize
import com.adriav.tcgpokemon.views.items.CollectionCardItem

@Composable
fun MyCollectionScreen(
    viewModel: MyCollectionViewModel,
    onCardClick: (String) -> Unit
) {
    val cards by viewModel.filteredCollection
        .collectAsState(initial = emptyList())

    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSet by viewModel.selectedSet.collectAsState()
    val availableSets = remember(cards) {
        cards.map { it.set to it.set }.distinct().map { (set) -> set }
    }


    Column {
        CollectionSearchBar(query = searchQuery, onQueryChange = viewModel::onSearchQuery)
        SetFilterDropdown(
            sets = availableSets,
            selectedSet = selectedSet,
            onSetSelected = viewModel::selectSet
        )
        CardFilterRow(selectedFilter, viewModel::setFilter)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
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
fun CardFilterRow(selectedFilter: CardFilter?, onFilterSelected: (CardFilter?) -> Unit) {
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
        // ALL (No filter applied)
        FilterChip(
            selected = selectedFilter == null,
            onClick = { onFilterSelected(null) },
            label = { Text(text = "All") }
        )

        // Energies
        energies.forEach { energy ->
            FilterChip(
                selected = selectedFilter is CardFilter.Energy && selectedFilter.energyType == energy,
                onClick = { onFilterSelected(CardFilter.Energy(energy)) },
                leadingIcon = {
                    EnergyIcon(energyType = energy.apiName, modifier = Modifier.height(20.dp))
                },
                label = { Text(text = energy.apiName) }
            )
        }

        // Trainer Category
        FilterChip(
            selected = selectedFilter is CardFilter.Category &&
                    selectedFilter.category == CardCategory.TRAINER,
            onClick = {
                onFilterSelected(CardFilter.Category(CardCategory.TRAINER))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            label = { Text("Trainer") }
        )

        // Energy Category
        FilterChip(
            selected = selectedFilter is CardFilter.Category &&
                    selectedFilter.category == CardCategory.ENERGY,
            onClick = {
                onFilterSelected(CardFilter.Category(CardCategory.ENERGY))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            label = { Text("Energy") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetFilterDropdown(
    sets: List<String>,
    selectedSet: String?,
    onSetSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(selectedSet) {
        if (!isTyping) {
            inputText = sets.firstOrNull { it == selectedSet } ?: ""
        }
    }

    val filteredSets = remember(inputText, sets) {
        sets.filter {
            it.normalize().contains(inputText.normalize(), ignoreCase = true)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = inputText,
            // value = sets.firstOrNull { it == selectedSet } ?: "All Sets",
            onValueChange = {
                isTyping = true
                inputText = it
                expanded = true

                if (it.isBlank()) {
                    onSetSelected(null)
                }
            },
            label = { Text("Sets") },
            placeholder = { Text("All Sets") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    expanded = false
                    isTyping = false
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier =
                Modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                isTyping = false
                expanded = false
            },
            modifier = Modifier.heightIn(max = 200.dp),
            scrollState = rememberScrollState()
        ) {
            // Opción para mostrar todos los sets
            DropdownMenuItem(
                text = { Text("All Sets") },
                onClick = {
                    onSetSelected(null)
                    inputText = ""
                    expanded = false
                    isTyping = false
                    keyboardController?.hide()
                }
            )
            filteredSets.forEach { set ->
                DropdownMenuItem(
                    text = {Text(set)},
                    onClick = {
                        onSetSelected(set)
                        inputText = set
                        expanded = false
                        isTyping = false
                        keyboardController?.hide()
                    }
                )
            }
        }
    }
}