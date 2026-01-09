package com.adriav.tcgpokemon

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.adriav.tcgpokemon.navigation.NavigationWrapper
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.ui.theme.TCGPokemonTheme
import com.adriav.tcgpokemon.ui.theme.ThemeViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokeApp : Application() {
    @Composable
    fun AppRoot() {
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
