package com.adriav.tcgpokemon.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Serie
import net.tcgdex.sdk.models.SetResume
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@HiltViewModel
class SingleSerieViewModel @Inject constructor(
    private val tcgdex: TCGdex
) : ViewModel() {
    private val _serie = MutableLiveData<Serie>()
    val serie: LiveData<Serie> = _serie
    private val _sets = MutableLiveData<List<SetResume>>()
    val sets: LiveData<List<SetResume>> = _sets
    private val _serieId = MutableLiveData<String>()
    val serieId: LiveData<String> = _serieId

    fun setSerieId(id: String) {
        _serieId.value = id
    }
    fun loadSerie() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tcgdex.fetchSerie(serieId.value)
                _serie.postValue(response!!)
                _sets.postValue(response.sets)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}