package com.adriav.tcgpokemon.views.singleview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.objects.TCGdexProvider
import com.adriav.tcgpokemon.views.items.SetItemView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.models.Serie
import net.tcgdex.sdk.models.SetResume

@Composable
fun SingleSerieScreen(serieID: String) {
    val tcgdex = TCGdexProvider.tcgdex
    var serie by remember { mutableStateOf<Serie?>(null) }
    var sets by remember { mutableStateOf<List<SetResume?>>(emptyList()) }

    LaunchedEffect(Unit) {
        val api = tcgdex
        serie = withContext(Dispatchers.IO) {
            api.fetchSerie(serieID)
        }
        sets = serie?.sets!!
    }

    if (serie == null) {
        CenteredProgressIndicator()
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            SerieHeader(serie!!, sets)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn(modifier = Modifier.padding(bottom = 32.dp)) {
                items(sets.size) {index ->
                    SetItemView(sets[index]!!)
                }
            }
        }
    }
}

@Composable
fun SerieHeader (serie: Serie, sets: List<SetResume?>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = serie.getLogoUrl(Extension.WEBP),
            contentDescription = serie.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.loading_progress_icon),
            error = painterResource(R.drawable.verror_code_vector_icon)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {
            Text(text = "Nombre: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = serie.name, fontSize = 20.sp)
        }
        Row {
            Text(text = "ID: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = serie.id, fontSize = 20.sp)
        }
        Row {
            Text(text = "Sets: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = sets.size.toString(), fontSize = 20.sp)

        }

    }

}