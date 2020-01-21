package com.example.pokemon.repository

import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonDetail
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PokemonRepository {

    fun getPokemonList(offset: Int): Single<ArrayList<Pokemon>> = RetroFitClient.INSTANCE.getPokemons(offset, 8)
        .map { it.results }


    fun getPokemonDetails(id: Int): Single<PokemonDetail> = RetroFitClient.INSTANCE.getPokemonDetails(id)
        .map { it }
}
