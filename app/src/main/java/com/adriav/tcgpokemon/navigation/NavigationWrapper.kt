package com.adriav.tcgpokemon.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.adriav.tcgpokemon.navigation.Routes.*
import com.adriav.tcgpokemon.views.allview.AllSeriesScreen
import com.adriav.tcgpokemon.views.allview.AllSetsScreen
import com.adriav.tcgpokemon.views.HomeScreen
import com.adriav.tcgpokemon.views.singleview.SingleSerieScreen
import com.adriav.tcgpokemon.views.singleview.SingleSetScreen

@Composable
fun NavigationWrapper() {
    val backStack = rememberNavBackStack(Home)
    NavDisplay(modifier = Modifier.padding(top = 16.dp),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                HomeScreen(navigateToAllSets = { backStack.add(AllSets) }, navigateToAllSeries = { backStack.add(AllSeries) })
            }
            entry<AllSeries> {
                AllSeriesScreen() { serieID ->
                    backStack.add(SingleSerie(serieID))
                }
            }
            entry<AllSets> {
                AllSetsScreen()
            }
            entry<SingleSerie> {
                SingleSerieScreen(it.id) {
                    setID -> backStack.add(SingleSet(setID))
                }
            }
            entry<SingleSet> {
                SingleSetScreen(it.id)
            }
        }
    )
}
