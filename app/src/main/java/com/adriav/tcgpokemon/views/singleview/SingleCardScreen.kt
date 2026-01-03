package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.models.SingleCardViewModel
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.Card

@Composable
fun SingleCardScreen(viewModel: SingleCardViewModel, cardID: String) {
    // Scroll Helper
    val scrollState = rememberScrollState()
    // Card Instance
    val card by viewModel.card.observeAsState(null)
    val cardName by viewModel.cardName.observeAsState("")
    val cardIllustrator by viewModel.cardIllustrator.observeAsState(null)
    val cardRarity by viewModel.cardRarity.observeAsState(null)
    val cardCategory by viewModel.cardCategory.observeAsState(null)
    val cardSet by viewModel.cardSet.observeAsState(null)
    val cardDexID by viewModel.dexID.observeAsState(null)
    val cardHP by viewModel.cardHP.observeAsState(null)
    val cardTypes by viewModel.cardTypes.observeAsState(null)
    val cardEvolveFrom by viewModel.evolveFrom.observeAsState(null)
    val cardDescription by viewModel.cardDescription.observeAsState(null)
    val cardAbilities by viewModel.cardAbilities.observeAsState(null)
    val cardAttacks by viewModel.cardAttacks.observeAsState(null)
    val cardWeaknesses by viewModel.cardWeaknesses.observeAsState(null)
    val cardResistances by viewModel.cardResistances.observeAsState(null)
    val cardRetreat by viewModel.cardRetreat.observeAsState(null)
    val cardEffect by viewModel.cardEffect.observeAsState(null)
    val cardTrainerType by viewModel.trainerType.observeAsState(null)
    val cardEnergyType by viewModel.energyType.observeAsState(null)

    // init card Model
    viewModel.setCardId(cardID)
    viewModel.loadCard()

    if (card == null) {
        CenteredProgressIndicator()
    } else {
        val imageURL = card!!.getImageUrl(Quality.HIGH, Extension.WEBP)
        Column {
            AppHeader(cardName)
            DisplayCardImage(imageURL, cardName)
            HorizontalDivider(Modifier.padding(vertical = 2.dp))
            ShowAttackAbilities(scrollState, card)
        }
    }
}

@Composable
fun DisplayCardImage(imageURL: String, cardName: String) {
    AsyncImage(
        model = imageURL,
        contentDescription = cardName,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun SingleCardBody (){

}

@Composable
fun ShowAttackAbilities(scrollState: ScrollState, card: Card?) {
    Column (Modifier.verticalScroll(scrollState, enabled = true, reverseScrolling = false)) {
        if (card == null) {
            CenteredProgressIndicator()
        } else {
            Text(text = card.toString())
        }
    }
}