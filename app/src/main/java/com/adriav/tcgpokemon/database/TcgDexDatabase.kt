package com.adriav.tcgpokemon.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adriav.tcgpokemon.database.dao.CardDao
import com.adriav.tcgpokemon.database.entity.CardEntity

@Database(entities = [CardEntity::class], version = 2)
abstract class TcgDexDatabase : RoomDatabase() {
    abstract fun getCardDao() : CardDao
}