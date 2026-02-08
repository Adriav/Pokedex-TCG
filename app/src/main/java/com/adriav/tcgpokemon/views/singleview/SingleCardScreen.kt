package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.adriav.tcgpokemon.objects.EnergyIconRow
import com.adriav.tcgpokemon.objects.RetreatCostIcons
import com.adriav.tcgpokemon.objects.WeakResIconRow
import com.adriav.tcgpokemon.objects.getTypeColor
import net.tcgdex.sdk.models.subs.CardAbility
import net.tcgdex.sdk.models.subs.CardAttack
import net.tcgdex.sdk.models.subs.CardWeakRes

@Composable
fun SingleCardScreen(
    viewModel: SingleCardViewModel,
    cardID: String,
    navigateToCardSet: (String) -> Unit
) {
    // Scroll Helper
    val scrollState = rememberScrollState()
    // Card Object
    val card by viewModel.card.observeAsState(null)
    // Card Attributes
    val cardName by viewModel.cardName.observeAsState("")
    val cardIllustrator by viewModel.cardIllustrator.observeAsState(null)
    val cardRarity by viewModel.cardRarity.observeAsState(null)
    val cardSet by viewModel.cardSet.observeAsState(null)
    val cardDexID by viewModel.dexID.observeAsState(null)
    val cardHP by viewModel.cardHP.observeAsState(null)
    val cardTypes by viewModel.cardTypes.observeAsState(null)
    val cardEvolveFrom by viewModel.evolveFrom.observeAsState(null)
    val cardStage by viewModel.cardStage.observeAsState(null)
    val cardAbilities by viewModel.cardAbilities.observeAsState(null)
    val cardAttacks by viewModel.cardAttacks.observeAsState(null)
    val cardWeaknesses by viewModel.cardWeaknesses.observeAsState(null)
    val cardResistances by viewModel.cardResistances.observeAsState(null)
    val cardRetreat by viewModel.cardRetreat.observeAsState(null)
    val cardEffect by viewModel.cardEffect.observeAsState(null)
    val cardTrainerType by viewModel.trainerType.observeAsState(null)
    val cardEnergyType by viewModel.energyType.observeAsState(null)
    val imageURL by viewModel.imageURL.observeAsState("")
    // Is Collected
    val isCollected by viewModel.isCollected.observeAsState(false)
    viewModel.getIsCollected()

    // init card Model
    viewModel.setCardId(cardID)
    viewModel.loadCard()

    // Display card
    if (card == null) {
        CenteredProgressIndicator()
    } else {
        Column(Modifier.verticalScroll(scrollState, enabled = true, reverseScrolling = false)) {
            AppHeader(cardName)
            DisplayCardImage(imageURL, cardName)
            HorizontalDivider(Modifier.padding(vertical = 2.dp))
            cardHP?.let { ShowTypeHP(cardTypes, it, cardStage, cardEvolveFrom) }
            cardAbilities?.let { ShowAbilities(it) }
            cardAttacks?.let { ShowAttacks(it) }
            DisplayCardDetails(
                cardSet,
                cardDexID?.get(0),
                cardIllustrator,
                cardRarity,
                cardEnergyType,
                cardTrainerType,
                navigateToCardSet
            )
            if (hasBattleTraits(cardWeaknesses, cardResistances, cardRetreat)) {
                DisplayBattleTraits(
                    cardWeaknesses,
                    cardResistances,
                    cardRetreat
                )
            }
            cardEffect?.let { DisplayCardEffect(cardEffect!!) }
            CollectionButton(cardTypes, isCollected, viewModel)
        }
    }
}

@Composable
fun CollectionButton(types: List<String>?, isCollected: Boolean, viewModel: SingleCardViewModel) {
    val typeColor = types?.get(0) ?: "Colorless"
    val contentColor: Color = if (typeColor == "Lightning") Color.Black else Color.White
    val addColors = ButtonColors(
        containerColor = getTypeColor(typeColor),
        disabledContentColor = Color.Unspecified,
        disabledContainerColor = Color.Unspecified,
        contentColor = contentColor
    )
    val removeColors = ButtonColors(
        containerColor = Color.Red,
        disabledContentColor = Color.Unspecified,
        disabledContainerColor = Color.Black,
        contentColor = Color.White
    )
    if (isCollected) {
        Button(
            onClick = { viewModel.removeFromCollection() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            colors = removeColors,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Remove from collection",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Button(
            onClick = { viewModel.addToCollection() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            colors = addColors,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Add to collection",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DisplayCardEffect(cardEffect: String?) {
    Card(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(all = 12.dp)) {
            Text(text = "Effect", fontSize = 20.sp, modifier = Modifier.fillMaxWidth())
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            Text(text = cardEffect ?: "")
        }
    }

}

@Composable
fun ShowTypeHP(types: List<String>?, hp: Int, cardStage: String?, cardEvolveFrom: String?) {
    val type = types?.get(0) ?: "Colorless"
    val contentColor: Color = if (type == "Lightning") Color.Black else Color.Unspecified
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getTypeColor(type),
            contentColor = contentColor
        ), modifier = Modifier.padding(all = 4.dp)
    ) {
        Column(modifier = Modifier.padding(all = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                types?.let { EnergyIconRow(it) }
                Row {
                    Text(text = "$hp", fontSize = 20.sp)
                    Text(text = "HP", fontSize = 10.sp)
                }
            }
            if (cardStage != null) {
                CardStageRow(cardStage)
            }
            if (cardEvolveFrom != null) {
                EvolvesFromRow(cardEvolveFrom)
            }
        }
    }
}

@Composable
private fun CardStageRow(cardStage: String) {
    HorizontalDivider(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Stage", fontSize = 20.sp)
        Text(text = cardStage)
    }
}

@Composable
private fun EvolvesFromRow(cardEvolvesFrom: String) {
    HorizontalDivider(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Evolves from:", fontSize = 20.sp)
        Text(text = cardEvolvesFrom)
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
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.BattleTraits)
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
                BattleTraitsValues(cardWeaknesses, cardResistances, cardRetreat)
            }
        }
    }
}

@Composable
fun BattleTraitsValues(
    cardWeaknesses: List<CardWeakRes>?,
    cardResistances: List<CardWeakRes>?,
    cardRetreat: Int?
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (cardWeaknesses != null) {
            WeakResIconRow(cardWeaknesses)
        } else {
            Text(text = "None")
        }
        if (cardResistances != null) {
            WeakResIconRow(cardResistances)
        } else {
            Text(text = "None")
        }
        if (cardRetreat != null) {
            RetreatCostIcons(cardRetreat)
        } else {
            Text(text = "Free")
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
        error = painterResource(R.drawable.card_back)
    )
}

@Composable
private fun ShowAbilities(cardAbilities: List<CardAbility>) {
    cardAbilities.forEach { ability ->
        Card(modifier = Modifier.padding(all = 8.dp)) {
            Box(modifier = Modifier.padding(all = 16.dp)) {
                Column {
                    Text(
                        text = ability.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.BattleTraits)
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
        if (attack.name != null || attack.effect != null) {
            Card(modifier = Modifier.padding(all = 8.dp)) {
                Box(modifier = Modifier.padding(all = 16.dp)) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            EnergyIconRow(attack.cost ?: emptyList())
                            Text(
                                text = attack.name ?: "",
                                fontSize = 18.sp,
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
}

@Composable
private fun DisplayCardDetails(
    cardSet: String?,
    cardDexID: Int?,
    cardIllustrator: String?,
    cardRarity: String?,
    cardEnergyType: String?,
    cardTrainerType: String?,
    navigateToCardSet: (String) -> Unit
) {
    Card(modifier = Modifier.padding(all = 8.dp)) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Column {
                cardSet?.let {
                    CardSetRow(it, navigateToCardSet)
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
                cardEnergyType?.let {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    EnergyTypeRow(it)
                }
                cardTrainerType?.let {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    TrainerTypeRow(it)
                }
            }
        }
    }
}

@Composable
fun TrainerTypeRow(trainerType: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Type", fontSize = 20.sp)
        Text(text = trainerType, fontSize = 20.sp)
    }
}

@Composable
fun EnergyTypeRow(energyType: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Type", fontSize = 20.sp)
        Text(text = energyType, fontSize = 20.sp)
    }
}

@Composable
private fun CardSetRow(cardSet: String, navigateToCardSet: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Set", fontSize = 20.sp)
        Box(modifier = Modifier.clickable { navigateToCardSet(cardSet) }) {
            Text(text = cardSet, fontSize = 20.sp, color = Color(0xFF237DB3))
        }
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
        Text(text = "Artist", fontSize = 20.sp)
        Text(text = cardIllustrator, fontSize = 20.sp)
    }
}

@Composable
private fun CardRarityRow(cardRarity: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Rarity", fontSize = 20.sp)
        Text(text = cardRarity, fontSize = 20.sp)
    }
}


private fun hasBattleTraits(
    weaknesses: List<CardWeakRes>?,
    resistances: List<CardWeakRes>?,
    retreat: Int?
): Boolean =
    !weaknesses.isNullOrEmpty() || !resistances.isNullOrEmpty() || (retreat != null)