package com.adriav.tcgpokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.adriav.tcgpokemon.navigation.NavigationWrapper
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.ui.theme.TCGPokemonTheme
import com.adriav.tcgpokemon.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val systemDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(Unit) {
                themeViewModel.initTheme(systemDarkTheme)
            }

            val isDarkTheme  by themeViewModel.isDarkTheme.collectAsState()

            if (isDarkTheme  == null){
                CenteredProgressIndicator()
            } else {
                TCGPokemonTheme(darkTheme = isDarkTheme!! ) {
                    Scaffold { paddingValues ->
                        NavigationWrapper(
                            isDarkTheme!!,
                            paddingValues
                        ) { themeViewModel.toggleTheme() }
                    }
                }
            }

        }
    }
}

/*
* Furret: swsh3-136
* Brock: gym1-15
* Mega Manectric ex: me01-050
* Alakazam: base1-1
* */