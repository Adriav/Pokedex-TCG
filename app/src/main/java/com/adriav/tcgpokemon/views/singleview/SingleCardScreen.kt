package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.models.SingleCardViewModel
import com.adriav.tcgpokemon.objects.AppHeader
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.subs.CardAbility
import net.tcgdex.sdk.models.subs.CardAttack
import net.tcgdex.sdk.models.subs.CardWeakRes

@Composable
fun SingleCardScreen(viewModel: SingleCardViewModel, cardID: String) { // ID: swsh3-136
    // Scroll Helper
    val scrollState = rememberScrollState()
    // Card Attributes
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
        Column(Modifier.verticalScroll(scrollState, enabled = true, reverseScrolling = false)) {
            AppHeader(cardName)
            DisplayCardImage(imageURL, cardName)
            HorizontalDivider(Modifier.padding(vertical = 2.dp))
            cardHP?.let { ShowTypeHP(cardTypes!![0], it) }
            cardAbilities?.let { ShowAbilities(it) }
            cardAttacks?.let { ShowAttacks(it) }
            DisplayCardDetails(
                cardSet,
                cardDexID?.get(0),
                cardIllustrator,
                cardRarity
            )
            cardRetreat?.let { DisplayBattleTraits(cardWeaknesses, cardResistances, cardRetreat) }
            // cardTypes?.let { Text(text = "Tipo: $it", fontSize = 20.sp) }
        }
    }
}

@Composable
fun ShowTypeHP(type: String, hp: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = GetTypeColor(type)
        ), modifier = Modifier.padding(all = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$type type", fontSize = 20.sp)
            Row {
                Text(text = "$hp", fontSize = 20.sp)
                Text(text = "HP", fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun DisplayBattleTraits(
    cardWeaknesses: List<CardWeakRes>?,
    cardResistances: List<CardWeakRes>?,
    cardRetreat: Int?
) {
    Card(modifier = Modifier.padding(all = 8.dp)) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Battle Traits",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Weakness", fontSize = 20.sp)
                    Text(text = "Resistance", fontSize = 20.sp)
                    Text(text = "Retreat", fontSize = 20.sp)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (cardWeaknesses != null) {
                        Text(text = "${cardWeaknesses[0].type} ${cardWeaknesses[0].value}")
                    } else {
                        Text(text = "n/a")
                    }
                    if (cardResistances != null) {
                        Text(text = "${cardResistances[0].type} ${cardResistances[0].value}")
                    } else {
                        Text(text = "n/a")
                    }
                    if (cardRetreat != null) {
                        Text(text = cardRetreat.toString())
                    } else {
                        Text(text = "n/a")
                    }
                }
            }
        }
    }
}


@Composable
private fun DisplayCardImage(imageURL: String, cardName: String) {
    AsyncImage(
        model = imageURL,
        contentDescription = cardName,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(450.dp),
        contentScale = ContentScale.Fit,
        placeholder = painterResource(R.drawable.loading_progress_icon),
        error = painterResource(R.drawable.verror_code_vector_icon)
    )
}

@Composable
private fun ShowAbilities(cardAbilities: List<CardAbility>) {
    Text(text = "HABILIDADES!", modifier = Modifier.padding(vertical = 4.dp))
    cardAbilities.forEach { ability ->
        Card(modifier = Modifier.padding(all = 8.dp)) {
            Box(modifier = Modifier.padding(all = 16.dp)) {
                Column {
                    Text(
                        text = "Cost: ${ability.type} - ${ability.name}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = ability.effect, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun ShowAttacks(cardAttacks: List<CardAttack>) {
    cardAttacks.forEach { attack ->
        Card(modifier = Modifier.padding(all = 8.dp)) {
            Box(modifier = Modifier.padding(all = 16.dp)) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Cost: ${attack.cost?.size} - ${attack.name}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        attack.damage?.let {
                            Text(
                                text = it,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    attack.effect?.let { Text(text = it, fontSize = 18.sp) }
                }
            }
        }
    }
}

@Composable
private fun DisplayCardDetails(
    cardSet: String?,
    cardDexID: Int?,
    cardIllustrator: String?,
    cardRarity: String?
) {
    Card(modifier = Modifier.padding(all = 8.dp)) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Column {
                cardSet?.let {
                    CardSetRow(it)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
                cardDexID?.let {
                    CardDexRow(it.toString())
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
                cardIllustrator?.let {
                    CardIllustratorRow(it)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
                cardRarity?.let { CardRarityRow(it) }
            }
        }
    }
}

@Composable
private fun CardSetRow(cardSet: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Set", fontSize = 20.sp)
        Text(text = cardSet, fontSize = 20.sp)
    }
}

@Composable
private fun CardDexRow(cardDexID: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "PokeDex N°", fontSize = 20.sp)
        Text(text = cardDexID, fontSize = 20.sp)
    }
}

@Composable
private fun CardIllustratorRow(cardIllustrator: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Artista", fontSize = 20.sp)
        Text(text = cardIllustrator, fontSize = 20.sp)
    }
}

@Composable
private fun CardRarityRow(cardRarity: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Rareza", fontSize = 20.sp)
        Text(text = cardRarity, fontSize = 20.sp)
    }
}

@Composable
fun GetTypeColor(colorName: String): Color {
    val context = LocalContext.current

    val colorResId = context.resources.getIdentifier(
        colorName,
        "color",
        context.packageName
    )

    return if (colorResId != 0) {
        colorResource(id = colorResId)
    } else {
        Color.Gray // fallback
    }
}