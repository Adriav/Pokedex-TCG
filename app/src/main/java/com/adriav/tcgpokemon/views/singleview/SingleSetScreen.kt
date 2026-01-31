package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.models.SingleSetViewModel
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.CardItemView
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.Set
import net.tcgdex.sdk.models.subs.SetCardCount

@Composable
fun SingleSetScreen(
    viewModel: SingleSetViewModel,
    setID: String,
    navigateToCard: (String) -> Unit,
    navigateToSerie: (String) -> Unit
) {
    val set by viewModel.set.observeAsState(null)
    val cards by viewModel.setCards.observeAsState(null)

    viewModel.setSetId(setID)
    viewModel.loadSet()

    if (set == null) {
        CenteredProgressIndicator()
    } else {
        Column {
            SetHeader(set!!, navigateToSerie)
            HorizontalDivider(
                modifier =
                    Modifier.padding(vertical = 8.dp)
            )
            CardsItems(cards!!, navigateToCard)
        }
    }
}

@Composable
fun SetHeader(set: Set, navigateToSerie: (String) -> Unit) {
    SetImage(set)
    SetInfo(set, navigateToSerie)
}

@Composable
fun SetImage(set: Set) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        AsyncImage(
            model = set.getLogoUrl(Extension.WEBP),
            contentDescription = set.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.loading_progress_icon),
            error = painterResource(R.drawable.series_placeholder)
        )
    }
}

@Composable
fun SetInfo(set: Set, navigateToSerie: (String) -> Unit) {
    val cardCount = set.cardCount
    val releaseDate = set.releaseDate
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = set.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp), contentAlignment = Alignment.Center
        ) {
            Row {
                Text(text = "Serie: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Box(modifier = Modifier.clickable { navigateToSerie(set.serie.id) }) {
                    Text(
                        text = set.serie.name,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (releaseDate != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    Text(text = "Release Date: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = releaseDate, fontSize = 20.sp)
                }

            }
        } else {
            CardCount(cardCount)
        }

    }
}

@Composable
fun CardsItems(cards: List<CardResume>, navigateToCard: (String) -> Unit) {
    LazyColumn {
        items(cards.size) { index ->
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(horizontal = 32.dp)
                    .clickable { navigateToCard(cards[index].id) }) {
                CardItemView(cards[index], index + 1)
            }
        }
    }
}

@Composable
fun CardCount(cardCount: SetCardCount) {
    // Card Counts
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {
            Text(text = "Official: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = cardCount.official.toString(), fontSize = 20.sp)
        }

        Row {
            Text(text = "Normal: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = cardCount.normal.toString(), fontSize = 20.sp)
        }

    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (cardCount.reverse > 0) {
            Row {
                Text(text = "Reverse: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = cardCount.reverse.toString(), fontSize = 20.sp)
            }
        }

        if (cardCount.holo > 0) {
            Row {
                Text(text = "Holo: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = cardCount.holo.toString(), fontSize = 20.sp)
            }
        }

        cardCount.firstEd?.let {
            if (it > 0) {
                Row {
                    Text(
                        text = "First Edition: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = cardCount.firstEd.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}