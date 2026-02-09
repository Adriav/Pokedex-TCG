package com.adriav.tcgpokemon.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.adriav.tcgpokemon.database.RemoteConfigManager
import com.adriav.tcgpokemon.objects.CardImageRepository
import com.adriav.tcgpokemon.objects.isVersionSupported
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppVersionViewModel @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager,
    private val imageRepository: CardImageRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _mustUpdate = MutableStateFlow(false)
    val mustUpdate = _mustUpdate

    init {
        viewModelScope.launch {
            imageRepository.loadImages()
        }
    }

    fun checkVersion() {
        viewModelScope.launch {
            remoteConfigManager.fetchAndActivate()

            val minVersion = remoteConfigManager.getMinVersionAllowed()
            val installedVersion = getApplication<Application>()
                .packageManager
                .getPackageInfo(
                    getApplication<Application>().packageName,
                    0
                )
                .versionName

            _mustUpdate.value = !isVersionSupported(installedVersion ?: "0.0.0", minVersion)
        }
    }
}