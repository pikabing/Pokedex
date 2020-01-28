package com.example.pokemon.data.repository

import android.annotation.SuppressLint
import com.example.pokemon.MyApplication
import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.Common
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

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

    fun getFavoritePokemonsList() = mAppDatabase.pokemonDao().fetchFavoriteList()


    fun getPokemonDetails(pokemon: Pokemon): Single<Pokemon> {

        return if (Common.isConnectedToNetwork(MyApplication.application.applicationContext))
            makePokemonDetailApiCall(pokemon)
        else
            getPokemonDetailFromDB(pokemon.id).switchIfEmpty(makePokemonDetailApiCall(pokemon))

    }

    @SuppressLint("CheckResult")
    fun setFavoritePokemon(pokemon: Pokemon, buttonState: Boolean) {
        Single.just(pokemon)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                if (buttonState)
                    favoritePokemon(it)
                else
                    unFavoritePokemon(it)
            }, {
                it.printStackTrace()
            })
    }

    private fun favoritePokemon(pokemon: Pokemon) {
        mAppDatabase.pokemonDao().setFavorite(pokemon.id, true)
    }


    private fun unFavoritePokemon(pokemon: Pokemon) {
        mAppDatabase.pokemonDao().setFavorite(pokemon.id, false)
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

    private fun getPokemonDetailFromDB(id: Int): Maybe<Pokemon> =
        mAppDatabase.pokemonDao().fetchPokemonDetail(id)


    private fun makePokemonListApiCall(offset: Int) = RetroFitClient.INSTANCE.getPokemons(offset, 8)
        .map { pokemonResponse ->
            val favoriteIds = mAppDatabase.pokemonDao().getFavoritePokemonsID()
            mAppDatabase.pokemonDao().insert(pokemonResponse.results.map {
                val tokens = it.url.split("/")
                it.id = tokens[tokens.lastIndex - 1].toInt()
                it.name = it.name.capitalize()
                if (it.id in favoriteIds) {
                    it.favorite = true
                }
                it
            })
            pokemonResponse.results
        }

    fun getPokemonListFromDB(): Single<List<Pokemon>> =
        mAppDatabase.pokemonDao().fetchPokemonList()

}

