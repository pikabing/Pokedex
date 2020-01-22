package com.example.pokemon.model

import com.google.gson.JsonArray

data class PokemonDetail(val height: Int,
                         val weight: Int,
                         val base_experience: Int,
                         val types: ArrayList<Types>,
                         val abilities: ArrayList<Abilities>,
                         val moves: JsonArray
                         )



data class Abilities(val ability: Ability)
data class Types(val type: Type)
data class Ability(val name: String)
data class Type(val name: String)



