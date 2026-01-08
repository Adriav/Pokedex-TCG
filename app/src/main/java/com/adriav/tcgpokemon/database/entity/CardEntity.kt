package com.adriav.tcgpokemon.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards_table")
data class CardEntity(
    @PrimaryKey val id: String, // Card unique ID
    @ColumnInfo(name = "name") val name: String, // Card name
    @ColumnInfo(name = "category") val category: String, // Card category = "Pokemon", "Trainer" or "Energy"
    @ColumnInfo(name = "rarity") val rarity: String, // Card rarity
    @ColumnInfo(name = "type") val type: String?, // Card ENERGY TYPE
    @ColumnInfo(name = "set") val set: String, // Card set
    @ColumnInfo(name = "image_url") val imageUrl: String, // Card image from the API

    @ColumnInfo(name = "added_at") val addedAt: Long = System.currentTimeMillis() // Date as Float
)