package com.adriav.tcgpokemon.objects

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardImageRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    private var imageMap: Map<String, String> = emptyMap()

    suspend fun loadImages() {
        try {
            remoteConfig.fetchAndActivate().await()

            val json = remoteConfig.getString("card_image_map")

            if (json.isNotBlank()) {
                imageMap = Json.decodeFromString(json)
            }

            Log.d("RemoteConfig", "Images loaded: ${imageMap.size}")

        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error loading images", e)
        }
    }

    fun getImage(cardId: String): String? {
        return imageMap[cardId]
    }
}
