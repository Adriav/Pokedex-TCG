package com.adriav.tcgpokemon.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAllSeries: () -> Unit,
    navigateToAllSets: () -> Unit,
    navigateToMyCollection: () -> Unit,
    navigateToSearchScreen: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Pokemon TCG Dex")},
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode)
                                Icons.Default.DarkMode
                            else Icons.Default.LightMode,
                            contentDescription = "Toggle Dark Mode"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeButton("All Series", navigateToAllSeries)
            HomeButton("All Sets", navigateToAllSets)
            HomeButton("Search Cards", navigateToSearchScreen)
            HomeButton("My Collection", navigateToMyCollection)
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}