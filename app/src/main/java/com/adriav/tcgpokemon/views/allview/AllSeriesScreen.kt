package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.adriav.tcgpokemon.models.AllSeriesViewModel
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.SerieItemView
import net.tcgdex.sdk.models.SerieResume

@Composable
fun AllSeriesScreen(viewModel: AllSeriesViewModel, navigateToSerie: (String) -> Unit) {
    val cardSeries: Array<SerieResume>? by viewModel.cardSeries.observeAsState(null)

    viewModel.loadAllSeries()
    if (cardSeries == null) {
        CenteredProgressIndicator()
    } else {
        SeriesItems(cardSeries, navigateToSerie)
    }
}

@Composable
fun SeriesItems(
    cardSeries: Array<SerieResume>?,
    navigateToSerie: (String) -> Unit
) {
    Column {
        AppHeader("All Series")
        LazyColumn {
            items(cardSeries!!.size) { index ->
                Box(
                    modifier = Modifier
                        .clickable {
                            navigateToSerie(cardSeries[index].id)
                        }) {
                    SerieItemView(cardSeries[index])
                }
            }
        }
    }
}