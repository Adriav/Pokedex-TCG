package com.adriav.tcgpokemon.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.adriav.tcgpokemon.models.AllSeriesViewModel
import com.adriav.tcgpokemon.models.HomeViewModel
import com.adriav.tcgpokemon.models.SingleSerieViewModel
import com.adriav.tcgpokemon.navigation.Routes.AllSeries
import com.adriav.tcgpokemon.navigation.Routes.AllSets
import com.adriav.tcgpokemon.navigation.Routes.Home
import com.adriav.tcgpokemon.navigation.Routes.SingleCard
import com.adriav.tcgpokemon.navigation.Routes.SingleSerie
import com.adriav.tcgpokemon.navigation.Routes.SingleSet
import com.adriav.tcgpokemon.views.HomeScreen
import com.adriav.tcgpokemon.views.allview.AllSeriesScreen
import com.adriav.tcgpokemon.views.allview.AllSetsScreen
import com.adriav.tcgpokemon.views.singleview.SingleCardScreen
import com.adriav.tcgpokemon.views.singleview.SingleSerieScreen
import com.adriav.tcgpokemon.views.singleview.SingleSetScreen

@Composable
fun NavigationWrapper() {
    val backStack = rememberNavBackStack(Home)
    NavDisplay(
        modifier = Modifier.padding(top = 16.dp),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    navigateToAllSets = { backStack.add(AllSets) },
                    navigateToAllSeries = { backStack.add(AllSeries) },
                    viewModel = homeViewModel
                )
            }
            entry<AllSeries> {
                val seriesViewModel = hiltViewModel<AllSeriesViewModel>()
                AllSeriesScreen (viewModel = seriesViewModel) { serieID ->
                    backStack.add(SingleSerie(serieID))
                }
            }
            entry<AllSets> {
                AllSetsScreen()
            }
            entry<SingleSerie> { args ->
                val viewModel = hiltViewModel<SingleSerieViewModel>()
                SingleSerieScreen(
                    viewModel = viewModel,
                    serieID = args.serieID,
                    navigateToSet = { setID ->
                        backStack.add(SingleSet(setID))
                    }
                )
            }
            entry<SingleSet> {
                SingleSetScreen(it.setID) { cardID ->
                    backStack.add(SingleCard(cardID))
                }
            }
            entry<SingleCard> {
                SingleCardScreen(it.cardID)
            }
        }
    )
}
