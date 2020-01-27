package com.example.pokemon.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonArray

@Entity(tableName = "pokemon")
data class Pokemon(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var name: String,
    var url: String,
    var height: Int?,
    var weight: Int?,
    var base_experience: Int?,
    var types: ArrayList<Types>?,
    var abilities: ArrayList<Abilities>?,
    var moves: JsonArray?,
    var favorite: Boolean = false
)


data class Abilities(val ability: Ability)
data class Types(val type: Type)
data class Ability(val name: String)
data class Type(val name: String)
