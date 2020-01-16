package com.example.pokemon.model

import com.google.gson.JsonArray

data class PokemonDetail(var height: Int,
                         var weight: Int,
                         var base_experience: Int,
                         var types: ArrayList<Types>,
                         var abilities: ArrayList<Abilities>,
                         var moves: JsonArray
                         )



data class Abilities(var ability: Ability)
data class Types(var type: Type)
data class Ability(var name: String)
data class Type(var name: String)



