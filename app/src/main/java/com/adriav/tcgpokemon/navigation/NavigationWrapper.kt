package com.adriav.tcgpokemon.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.adriav.tcgpokemon.models.AllSeriesViewModel
import com.adriav.tcgpokemon.models.AllSetsViewModel
import com.adriav.tcgpokemon.models.MyCollectionViewModel
import com.adriav.tcgpokemon.models.SearchCardViewModel
import com.adriav.tcgpokemon.models.SingleCardViewModel
import com.adriav.tcgpokemon.models.SingleSerieViewModel
import com.adriav.tcgpokemon.models.SingleSetViewModel
import com.adriav.tcgpokemon.navigation.Routes.AllSeries
import com.adriav.tcgpokemon.navigation.Routes.AllSets
import com.adriav.tcgpokemon.navigation.Routes.Home
import com.adriav.tcgpokemon.navigation.Routes.SingleCard
import com.adriav.tcgpokemon.navigation.Routes.SingleSerie
import com.adriav.tcgpokemon.navigation.Routes.SingleSet
import com.adriav.tcgpokemon.views.HomeScreen
import com.adriav.tcgpokemon.views.MyCollectionScreen
import com.adriav.tcgpokemon.views.allview.AllSeriesScreen
import com.adriav.tcgpokemon.views.allview.AllSetsScreen
import com.adriav.tcgpokemon.views.search.SearchCardScreen
import com.adriav.tcgpokemon.views.singleview.SingleCardScreen
import com.adriav.tcgpokemon.views.singleview.SingleSerieScreen
import com.adriav.tcgpokemon.views.singleview.SingleSetScreen

@Composable
fun NavigationWrapper(isDarkMode: Boolean, paddingValues: PaddingValues, onToggleTheme: () -> Unit) {
    val backStack = rememberNavBackStack(Home)

    NavDisplay(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                HomeScreen(
                    navigateToAllSets = { backStack.add(AllSets) },
                    navigateToAllSeries = { backStack.add(AllSeries) },
                    navigateToMyCollection = { backStack.add(Routes.MyCollection) },
                    navigateToSearchScreen = { backStack.add(Routes.CardSearchResult) },
                    isDarkMode = isDarkMode,
                    onToggleTheme = onToggleTheme
                )
            }
            entry<AllSeries> {
                val allSeriesViewModel = hiltViewModel<AllSeriesViewModel>()
                AllSeriesScreen(viewModel = allSeriesViewModel) { serieID ->
                    backStack.add(SingleSerie(serieID))
                }
            }
            entry<AllSets> {
                val allSetsViewModel = hiltViewModel<AllSetsViewModel>()
                AllSetsScreen(viewModel = allSetsViewModel) { setID ->
                    backStack.add(SingleSet(setID))
                }
            }
            entry<SingleSerie> { args ->
                val singleSerieViewModel = hiltViewModel<SingleSerieViewModel>()
                SingleSerieScreen(
                    viewModel = singleSerieViewModel,
                    serieID = args.serieID,
                    navigateToSet = { setID ->
                        backStack.add(SingleSet(setID))
                    }
                )
            }
            entry<SingleSet> { args ->
                val singleSetViewModel = hiltViewModel<SingleSetViewModel>()
                SingleSetScreen(
                    viewModel = singleSetViewModel,
                    setID = args.setID
                ) { cardID ->
                    backStack.add(SingleCard(cardID))
                }
            }
            entry<SingleCard> { args ->
                val singleCardViewModel = hiltViewModel<SingleCardViewModel>()
                SingleCardScreen(
                    singleCardViewModel,
                    args.cardID,
                    navigateToCardSet = { setID ->
                        backStack.add(SingleSet(setID))
                    })
            }
            entry<Routes.MyCollection> {
                val myCollectionViewModel = hiltViewModel<MyCollectionViewModel>()
                MyCollectionScreen(viewModel = myCollectionViewModel) { cardID ->
                    backStack.add(SingleCard(cardID))
                }
            }
            entry<Routes.CardSearchResult> {
                val searchCardViewModel = hiltViewModel<SearchCardViewModel>()
                SearchCardScreen(viewModel = searchCardViewModel) { cardID ->
                    val clickedAt: Long = System.currentTimeMillis()
                    Log.i("card", "$clickedAt: $cardID")
                    backStack.add(SingleCard(cardID))
                }
            }
        }
    )
}
