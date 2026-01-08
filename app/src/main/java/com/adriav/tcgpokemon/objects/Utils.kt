package com.adriav.tcgpokemon.objects

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.R
import net.tcgdex.sdk.models.subs.CardWeakRes
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AppHeader(text: String = "Todas las ...") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)

    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
    HorizontalDivider(Modifier.padding(bottom = 4.dp))
}

@Composable
fun CenteredProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(width = 100.dp, height = 100.dp),
            color = Color(0xFF2D6BE0),
            strokeWidth = 8.dp
        )
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
fun getTypeColor(colorName: String): Color {
    val context = LocalContext.current

    val colorResId = context.resources.getIdentifier(
        colorName,
        "color",
        context.packageName
    )

    return if (colorResId != 0) {
        colorResource(id = colorResId)
    } else {
        colorResource(R.color.Colorless)
    }
}

sealed class EnergyType(
    val apiName: String,
    @DrawableRes val icon: Int
) {
    object Colorless : EnergyType("Colorless", R.drawable.colorless)
    object Darkness : EnergyType("Darkness", R.drawable.darkness)
    object Fairy : EnergyType("Fairy", R.drawable.fairy)
    object Water : EnergyType("Water", R.drawable.water)
    object Fire : EnergyType("Fire", R.drawable.fire)
    object Grass : EnergyType("Grass", R.drawable.grass)
    object Lightning : EnergyType("Lightning", R.drawable.lightning)
    object Metal : EnergyType("Metal", R.drawable.metal)
    object Psychic : EnergyType("Psychic", R.drawable.psychic)
    object Fighting : EnergyType("Fighting", R.drawable.fighting)
    object Dragon : EnergyType("Dragon", R.drawable.dragon)

    companion object {
        fun fromApi(value: String): EnergyType =
            listOf(
                Colorless, Darkness, Fairy, Water, Fire,
                Grass, Lightning, Metal, Psychic, Fighting, Dragon
            ).firstOrNull { it.apiName.equals(value, true) }
                ?: Colorless
    }
}

@Composable
fun EnergyIcon(
    energyType: String,
    modifier: Modifier = Modifier
) {
    val energyType = EnergyType.fromApi(energyType)
    Image(
        painter = painterResource(id = energyType.icon),
        contentDescription = energyType.apiName,
        modifier = modifier.size(25.dp)
    )
}

@Composable
fun EnergyIconRow(types: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        types.forEach { energy ->
            EnergyIcon(
                energyType = energy
            )
        }
    }
}

@Composable
fun WeakResIconRow(weakRes: List<CardWeakRes>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        weakRes.forEach { element ->
            EnergyIcon(
                energyType = element.type
            )
            Text(text = element.value.toString())
        }
    }
}

@Composable
fun RetreatCostIcons(cost: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (cost <= 1) {
            Text(text = "Free")
        } else {
            for (i in 1..cost) {
                Image(
                    painter = painterResource(R.drawable.colorless),
                    contentDescription = "Colorless",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

fun Long.toReadableDate(
    pattern: String = "dd/MM/yyyy"
) : String {
    val formatter = DateTimeFormatter
        .ofPattern(pattern, Locale.getDefault())

    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(formatter)
}