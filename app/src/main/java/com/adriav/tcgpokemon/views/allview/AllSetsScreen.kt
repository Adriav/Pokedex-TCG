package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.adriav.tcgpokemon.models.AllSetsViewModel
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.SetItemView
import net.tcgdex.sdk.models.SetResume

@Composable
fun AllSetsScreen(viewModel: AllSetsViewModel, navigateToSet: (String) -> Unit) {
    val cardSets: Array<SetResume>? by viewModel.cardSets.observeAsState(null)
    viewModel.loadAllSets()

    if (cardSets == null) {
        CenteredProgressIndicator()
    } else {
        Column {
            AppHeader("All Sets")
            SetsItems(cardSets!!, navigateToSet)
        }
    }
}

@Composable
fun SetsItems(cardSets: Array<SetResume>, navigateToSet: (String) -> Unit) {
    LazyColumn {
        items(cardSets.size) { index ->
            Box(
                modifier = Modifier
                    .clickable {
                        navigateToSet(cardSets[index].id)
                    }
            ) {
                SetItemView(cardSets[index])
            }
        }
    }
}