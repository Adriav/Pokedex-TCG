package com.adriav.tcgpokemon.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.objects.TCGdexProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SingleCardViewModel @Inject constructor(
    private val tcgdex: TCGdex = TCGdexProvider.provideTCGdex()
) : ViewModel() {
    private val _cardID = MutableLiveData<String>()
    val cardID = _cardID
    private val _card = MutableLiveData<Card>()
    val card = _card

    fun setCardId(id: String) {
        _cardID.value = id
    }

    fun loadCard(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tcgdex.fetchCard(_cardID.value)
                _card.postValue(response!!)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }
}