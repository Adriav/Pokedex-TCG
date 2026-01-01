package com.adriav.tcgpokemon.objects

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.tcgdex.sdk.TCGdex
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TCGdexProvider {
    @Singleton
    @Provides
    fun provideTCGdex(): TCGdex {
        return TCGdex("en")
    }
}