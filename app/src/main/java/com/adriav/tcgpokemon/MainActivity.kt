package com.adriav.tcgpokemon

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adriav.tcgpokemon.navigation.NavigationWrapper
import com.adriav.tcgpokemon.ui.theme.TCGPokemonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCGPokemonTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    NavigationWrapper()
                }
            }
        }
    }
}
