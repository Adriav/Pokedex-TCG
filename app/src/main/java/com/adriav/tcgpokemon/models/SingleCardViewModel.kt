package com.adriav.tcgpokemon.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.subs.CardAbility
import net.tcgdex.sdk.models.subs.CardAttack
import net.tcgdex.sdk.models.subs.CardWeakRes
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@HiltViewModel
class SingleCardViewModel @Inject constructor(
    private val tcgdex: TCGdex
) : ViewModel() {
    private val _card = MutableLiveData<Card>()
    val card = _card
    private val _cardID = MutableLiveData<String>()
    private val _cardName = MutableLiveData<String>()
    val cardName = _cardName
    private val _cardIllustrator = MutableLiveData<String?>()
    val cardIllustrator = _cardIllustrator
    private val _cardRarity = MutableLiveData<String>()
    val cardRarity = _cardRarity
    private val _cardCategory = MutableLiveData<String>()
    val cardCategory = _cardCategory

    private val _cardSet = MutableLiveData<String>()
    val cardSet = _cardSet
    private val _dexID = MutableLiveData<List<Int>?>()
    val dexID = _dexID
    private val _cardHP = MutableLiveData<Int?>()
    val cardHP = _cardHP
    private val _cardTypes = MutableLiveData<List<String>?>()
    val cardTypes = _cardTypes
    private val _evolveFrom = MutableLiveData<String?>()
    val evolveFrom = _evolveFrom
    private val _cardDescription = MutableLiveData<String?>()
    val cardDescription = _cardDescription
    private val _cardAbilities = MutableLiveData<List<CardAbility>>()
    val cardAbilities = _cardAbilities
    private val _cardAttacks = MutableLiveData<List<CardAttack>>()
    val cardAttacks = _cardAttacks
    private val _cardWeaknesses = MutableLiveData<List<CardWeakRes>>()
    val cardWeaknesses = _cardWeaknesses
    private val _cardResistances = MutableLiveData<List<CardWeakRes>>()
    val cardResistances = _cardResistances
    private val _cardRetreat = MutableLiveData<Int?>()
    val cardRetreat = _cardRetreat
    private val _cardEffect = MutableLiveData<String?>()
    val cardEffect = _cardEffect
    private val _trainerType = MutableLiveData<String?>()
    val trainerType = _trainerType
    private val _energyType = MutableLiveData<String?>()
    val energyType = _energyType

    fun setCardId(id: String) {
        _cardID.value = id
    }

    fun loadCard() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tcgdex.fetchCard(_cardID.value)
                _card.postValue(response!!)
                _cardName.postValue(response.name)
                _cardIllustrator.postValue(response.illustrator)
                _cardRarity.postValue(response.rarity)
                _cardCategory.postValue(response.category)
                _cardSet.postValue(response.set.name)
                _dexID.postValue(response.dexId)
                _cardHP.postValue(response.hp)
                _cardTypes.postValue(response.types)
                _evolveFrom.postValue(response.evolveFrom)
                _cardDescription.postValue(response.description)
                _cardAbilities.postValue(response.abilities)
                _cardAttacks.postValue(response.attacks)
                _cardWeaknesses.postValue(response.weaknesses)
                _cardResistances.postValue(response.resistances)
                _cardRetreat.postValue(response.retreat)
                _cardEffect.postValue(response.effect)
                _trainerType.postValue(response.trainerType)
                _energyType.postValue(response.energyType)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}