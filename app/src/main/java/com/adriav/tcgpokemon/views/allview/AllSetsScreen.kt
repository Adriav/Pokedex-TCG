package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.adriav.tcgpokemon.views.items.SetItemView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.models.SetResume

@Composable
fun AllSetsScreen() {
    val tcgdex = TCGdexProvider.provideTCGdex()
    var cardSets: Array<SetResume>? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        cardSets = withContext(Dispatchers.IO) {
            tcgdex.fetchSets()
        }
    }

    if (cardSets == null) {
        CenteredProgressIndicator()
    } else {
        Column {
            AppHeader("Todas los Sets")
            LazyColumn(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                items(cardSets!!.size) { index ->
                    SetItemView(cardSets!![index])
                }
            }
        }
    }
}