package com.example.pokemon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
class PokemonEntity {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

    var name: String = ""
    var height: Int = 0
    var weight: Int = 0
    var points: Int = 0
    var moves: Int = 0

}