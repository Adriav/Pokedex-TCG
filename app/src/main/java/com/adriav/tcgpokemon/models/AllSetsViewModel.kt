package com.adriav.tcgpokemon.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.objects.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.SetResume
import javax.inject.Inject

@HiltViewModel
class AllSetsViewModel @Inject constructor(private val tcgdex: TCGdex) : ViewModel() {

    private val _allSets = MutableStateFlow<Array<SetResume>>(emptyArray())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredSets: StateFlow<Array<SetResume>> =
        combine(_allSets, _searchQuery) { sets, query ->
            val normalizedQuery = query.normalize()

            if (normalizedQuery.isBlank()) {
                sets
            } else {
                sets
                    .filter { set ->
                        set.name.normalize().contains(normalizedQuery, ignoreCase = true)
                    }
                    .toTypedArray()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyArray()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _allSets.value = tcgdex.fetchSets()!!
            sortSets()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun sortSets() {
        _allSets.value.sortWith(compareBy { it.name })
    }
}