package com.example.pokemon.data.repository

import android.util.Log
import com.example.pokemon.MyApplication
import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.Common
import io.reactivex.Maybe
import io.reactivex.Single

class PokemonRepository private constructor(private val mAppDatabase: AppDatabase) {

    companion object {
        private var instance: PokemonRepository? = null
        fun getInstance(appDatabase: AppDatabase): PokemonRepository {
            if (instance == null) {
                instance = PokemonRepository(appDatabase)
            }
            return instance!!
        }
    }

    fun getPokemonList(offset: Int): Single<List<Pokemon>> {

        return if (Common.isConnectedToNetwork(MyApplication.application.applicationContext))
            makePokemonListApiCall(offset)
        else
            getPokemonListFromDB()

    }


    fun getPokemonDetails(pokemon: Pokemon): Single<Pokemon> {

        return if (Common.isConnectedToNetwork(MyApplication.application.applicationContext))
            makePokemonDetailApiCall(pokemon)
        else
            getPokemonDetailFromDB(pokemon.id).switchIfEmpty(makePokemonDetailApiCall(pokemon))

    }

    private fun makePokemonDetailApiCall(pokemon: Pokemon): Single<Pokemon> {
        return RetroFitClient.INSTANCE.getPokemonDetails(pokemon.id)
            .map {
                it.id = pokemon.id
                it.name = pokemon.name
                it.url = pokemon.url
                mAppDatabase.pokemonDao().update(it)
                it
            }
    }

    private fun getPokemonDetailFromDB(id: Int): Maybe<Pokemon> {
        Log.e("TAG","DETAIL DB")
        return mAppDatabase.pokemonDao().fetchPokemonDetail(id)
    }

    private fun makePokemonListApiCall(offset: Int) = RetroFitClient.INSTANCE.getPokemons(offset, 8)
        .map {
            mAppDatabase.pokemonDao().insert(it.results)
            it.results
        }

    private fun getPokemonListFromDB(): Single<List<Pokemon>> {
        return mAppDatabase.pokemonDao().fetchPokemonList()
    }
}

