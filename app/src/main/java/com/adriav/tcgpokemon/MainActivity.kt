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
import androidx.hilt.navigation.compose.hiltViewModel
import com.adriav.tcgpokemon.models.SingleCardViewModel
import com.adriav.tcgpokemon.navigation.NavigationWrapper
import com.adriav.tcgpokemon.ui.theme.TCGPokemonTheme
import com.adriav.tcgpokemon.views.singleview.SingleCardScreen
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
                        .padding(top = 6.dp)
                ) {
                     NavigationWrapper()
//                    val singleCardViewModel = hiltViewModel<SingleCardViewModel>()
//                    SingleCardScreen(singleCardViewModel, "base1-1")
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