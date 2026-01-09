package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adriav.tcgpokemon.R
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.models.SerieResume

@Composable
fun SerieItemView(serieResume: SerieResume) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
        ) {
            val serieLogo = serieResume.getLogoUrl(Extension.WEBP)
            AsyncImage(
                model = serieLogo,
                contentDescription = serieResume.id,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.series_placeholder)
            )
            Column(modifier = Modifier.padding(start = 16.dp).fillMaxSize()) {
                Text(text = "Name: ${serieResume.name}")
                Text(text = "ID: ${serieResume.id}")
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
