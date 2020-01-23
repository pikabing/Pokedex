package com.example.pokemon.model

import com.google.gson.JsonArray
import java.io.Serializable

data class Pokemon(var name:String,
                   val url: String,
                   var id: String,
                   val height: Int?,
                   val weight: Int?,
                   val base_experience: Int?,
                   val types: ArrayList<Types>?,
                   val abilities: ArrayList<Abilities>?,
                   val moves: JsonArray?
) : Serializable


data class Abilities(val ability: Ability)
data class Types(val type: Type)
data class Ability(val name: String)
data class Type(val name: String)
