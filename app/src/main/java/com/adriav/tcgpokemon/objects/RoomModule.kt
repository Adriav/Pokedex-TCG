package com.adriav.tcgpokemon.objects

import android.content.Context
import androidx.room.Room
import com.adriav.tcgpokemon.database.TcgDexDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val TCGDEX_DATABASE_NAME = "tcgdex_database"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TcgDexDatabase::class.java,
        TCGDEX_DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCardDao(db: TcgDexDatabase) = db.getCardDao()

}