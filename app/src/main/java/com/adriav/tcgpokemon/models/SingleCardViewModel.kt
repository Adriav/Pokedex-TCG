package com.adriav.tcgpokemon.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.database.entity.CardEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.subs.CardAbility
import net.tcgdex.sdk.models.subs.CardAttack
import net.tcgdex.sdk.models.subs.CardWeakRes
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@HiltViewModel
class SingleCardViewModel @Inject constructor(
    private val tcgdex: TCGdex,
    private val dao: CardDao
) : ViewModel() {
    private val _isCollected = MutableLiveData<Boolean>()
    val isCollected = _isCollected
    private val _card = MutableLiveData<Card>()
    val card = _card
    private val _cardID = MutableLiveData<String>()
    private val _cardCategory = MutableLiveData<String>()
    private val _cardName = MutableLiveData<String>()
    val cardName = _cardName
    private val _cardIllustrator = MutableLiveData<String?>()
    val cardIllustrator = _cardIllustrator
    private val _cardRarity = MutableLiveData<String>()
    val cardRarity = _cardRarity
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
    private val _cardStage = MutableLiveData<String?>()
    val cardStage = _cardStage
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
                _cardCategory.postValue(response.category)
                _cardName.postValue(response.name)
                _cardIllustrator.postValue(response.illustrator)
                _cardRarity.postValue(response.rarity)
                _cardSet.postValue(response.set.name)
                _dexID.postValue(response.dexId)
                _cardHP.postValue(response.hp)
                _cardTypes.postValue(response.types)
                _evolveFrom.postValue(response.evolveFrom)
                _cardStage.postValue(response.stage)
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

    fun getIsCollected() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = dao.cardExists(_cardID.value)
                _isCollected.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToCollection(){
        viewModelScope.launch(Dispatchers.IO) {
            val cardEntity = CardEntity(
                id = _cardID.value,
                name = _cardName.value,
                category = _cardCategory.value,
                rarity = _cardRarity.value,
                type = _cardTypes.value?.get(0),
                set = _cardSet.value,
                imageUrl = card.value!!.getImageUrl(Quality.HIGH, Extension.WEBP)
            )
            dao.insertCard(cardEntity)
            _isCollected.postValue(true)
        }
    }

    fun removeFromCollection(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCardById(_cardID.value)
            _isCollected.postValue(false)
        }
    }
}