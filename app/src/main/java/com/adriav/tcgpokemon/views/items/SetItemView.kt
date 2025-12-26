package com.adriav.tcgpokemon.views.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import net.tcgdex.sdk.models.SetResume

@Composable
// Vista de un UNICO SET a modo de item en un listado
fun SetItemView(setResume: SetResume) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
        ) {
            val setLogo = setResume.getLogoUrl(Extension.WEBP)
            AsyncImage(
                model = setLogo,
                contentDescription = setResume.id,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.loading_progress_icon),
                error = painterResource(R.drawable.verror_code_vector_icon)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                // Text(text = "ID: ${setResume.id}")
                Text(text = "Nombre: ${setResume.name}")
                Text(text = "Total: ${setResume.cardCount.total}")
                Text(text = "Oficial: ${setResume.cardCount.official}")
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }

    }
}