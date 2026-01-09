package com.adriav.tcgpokemon.ui.theme

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun initTheme(isSystemDark: Boolean) {
        if (_isDarkTheme.value == null){
            _isDarkTheme.value = isSystemDark
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !(_isDarkTheme.value ?: false)
    }
}