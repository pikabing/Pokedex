package com.example.pokemon.repository

import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonDetail
import io.reactivex.Single

class PokemonRepository private constructor(){

    private object HOLDER {
        val INSTANCE = PokemonRepository()
    }

    companion object {
        val instance: PokemonRepository by lazy { HOLDER.INSTANCE }
    }

    fun getPokemonList(offset: Int): Single<ArrayList<Pokemon>> = RetroFitClient.INSTANCE.getPokemons(offset, 8)
        .map { it.results }


    fun getPokemonDetails(id: Int): Single<PokemonDetail> = RetroFitClient.INSTANCE.getPokemonDetails(id)
}
