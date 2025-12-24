package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.objects.TCGdexProvider
import com.adriav.tcgpokemon.views.items.SerieItemView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.models.SerieResume

@Composable
fun AllSeriesScreen(navigateToSerie: (String) -> Unit) {
    val tcgdex = TCGdexProvider.tcgdex
    var cardSeries: Array<SerieResume>? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        cardSeries = withContext(Dispatchers.IO) {
            tcgdex.fetchSeries()
        }
    }

    if (cardSeries == null) {
        CenteredProgressIndicator()
    } else {
        Column {
            AppHeader("Todas las Series")
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 32.dp)
            ) {
                items(cardSeries!!.size) { index ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                navigateToSerie(cardSeries!![index].id)
                            }) {
                        SerieItemView(cardSeries!![index])
                    }
                }
            }
        }
    }
}