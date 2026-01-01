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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun SingleSetScreen(viewModel: SingleSetViewModel, setID: String, navigateToCard: (String) -> Unit) {
    val set by viewModel.set.observeAsState(null)
    val cards by viewModel.setCards.observeAsState(null)

    viewModel.setSetId(setID)
    viewModel.loadSet()

    if (set == null) {
        CenteredProgressIndicator()
    } else {
        Column {
            SetHeader(set!!)
            HorizontalDivider(
                modifier =
                    Modifier.padding(vertical = 8.dp)
            )
            CardsItems(cards!!, navigateToCard)
        }
    }
}

@Composable
fun SetHeader(set: Set) {
    val cardCount = set.cardCount
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = set.getLogoUrl(Extension.WEBP),
            contentDescription = set.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.loading_progress_icon),
            error = painterResource(R.drawable.verror_code_vector_icon)
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Text(text = "Oficial:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = cardCount.official.toString(), fontWeight = FontWeight.Bold)
            }

            Row {
                Text(text = "Normal:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = cardCount.normal.toString(), fontWeight = FontWeight.Bold)
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (cardCount.reverse > 0) {
                Row {
                    Text(text = "Reverse:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = cardCount.reverse.toString(), fontWeight = FontWeight.Bold)
                }
            }

            if (cardCount.holo > 0) {
                Row {
                    Text(text = "Holo:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = cardCount.holo.toString(), fontWeight = FontWeight.Bold)
                }
            }

            cardCount.firstEd?.let {
                if (it > 0) {
                    Row {
                        Text(
                            text = "First Edition:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(text = cardCount.firstEd.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }

        }
    }
}

@Composable
fun CardsItems(cards: List<CardResume>, navigateToCard: (String) -> Unit) {
    LazyColumn(modifier = Modifier.padding(bottom = 32.dp)) {
        items(cards.size) { index ->
            Box (modifier = Modifier.padding(vertical = 16.dp)
                .clickable{navigateToCard(cards[index].id)}) {
                CardItemView(cards[index], index + 1)
            }
        }
    }
}