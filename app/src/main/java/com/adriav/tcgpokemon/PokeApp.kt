package com.adriav.tcgpokemon

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adriav.tcgpokemon.ui.theme.ThemeViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokeApp : Application()
/*
{
    @Composable
    fun AppRoot() {
        val themeViewModel: ThemeViewModel = viewModel()
        val isDarkMode by themeViewModel.isDarkTheme.collectAsState()

        DevicesTheme(darkTheme = isDarkMode) {

        }
    }
}
*/