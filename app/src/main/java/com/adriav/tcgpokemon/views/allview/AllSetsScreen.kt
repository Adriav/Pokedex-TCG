package com.adriav.tcgpokemon.views.allview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adriav.tcgpokemon.R
import com.adriav.tcgpokemon.models.AllSetsViewModel
import com.adriav.tcgpokemon.objects.CenteredProgressIndicator
import com.adriav.tcgpokemon.views.items.SetItemView
import net.tcgdex.sdk.models.SetResume

@Composable
fun AllSetsScreen(viewModel: AllSetsViewModel, navigateToSet: (String) -> Unit) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sets by viewModel.filteredSets.collectAsState()
    Column {
        AllSetsHeader(searchQuery, viewModel)
        if (sets.isEmpty() && searchQuery.isNotEmpty()) {
            DisplaySearchError()
        } else if (sets.isEmpty()) {
            CenteredProgressIndicator()
        } else {
            SetsItems(sets, navigateToSet)

        }
    }
}

@Composable
fun SetsItems(cardSets: Array<SetResume>, navigateToSet: (String) -> Unit) {
    LazyColumn {
        items(cardSets.size) { index ->
            Box(
                modifier = Modifier
                    .clickable {
                        navigateToSet(cardSets[index].id)
                    }
            ) {
                SetItemView(cardSets[index])
            }
        }
    }
}

@Composable
fun AllSetsHeader(searchQuery: String, viewModel: AllSetsViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)

        ) {
            Text(
                text = "All Sets",
                fontSize = 28.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            label = { Text("Search sets") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
    HorizontalDivider(Modifier.padding(bottom = 4.dp))
}


@Composable
fun DisplaySearchError() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.verror_code_vector_icon),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "No Sets Found...",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}