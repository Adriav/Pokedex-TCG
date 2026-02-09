package com.adriav.tcgpokemon.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.objects.CardImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.Set
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@HiltViewModel
class SingleSetViewModel @Inject constructor(
    private val tcgdex: TCGdex,
    private val cardImageRepository: CardImageRepository
) : ViewModel() {
    private val _setId = MutableLiveData<String>()
    private val _set = MutableLiveData<Set>()
    val set = _set
    private val _setCards = MutableLiveData<List<CardResume>>()
    val setCards = _setCards

    fun setSetId(setId: String) {
        _setId.value = setId
    }

    fun loadSet() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tcgdex.fetchSet(_setId.value)
                _set.postValue(response!!)
                _setCards.postValue(response.cards)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getImageRepository(): CardImageRepository {
        return cardImageRepository
    }
}