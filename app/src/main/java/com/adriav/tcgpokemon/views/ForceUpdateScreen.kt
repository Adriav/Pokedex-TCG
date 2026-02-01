package com.adriav.tcgpokemon.views

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.adriav.tcgpokemon.R

@Composable
fun ForceUpdateScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
                    .size(250.dp, 250.dp)
                    .offset(x = -(16).dp),
                painter = painterResource(R.drawable.update_needed),
                contentDescription = "Update Needed",
                contentScale = ContentScale.Fit,

                )
            Text(
                text = "Update Needed!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Looks like you are using an old version."
                    )
                    Text(
                        text = "Please update to access the application."
                    )
                }
            }

            Button(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/Adriav/Pokedex-TCG/releases/tag/v1.0.0".toUri()
                )
                context.startActivity(intent)
            }) {
                Text(
                    text = "Update",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}