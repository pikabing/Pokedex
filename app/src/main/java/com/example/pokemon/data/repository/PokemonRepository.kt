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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository
@Inject
constructor(
    private val mAppDatabase: AppDatabase,
    private val application: MyApplication) {

    fun getPokemonList(offset: Int): Single<List<Pokemon>> {

        return if (Common.isConnectedToNetwork(application))
            makePokemonListApiCall(offset)
        else
            getPokemonListFromDB()

    }

    fun getFavoritePokemonsList() = mAppDatabase.pokemonDao().fetchFavoriteList()


    fun getPokemonDetails(pokemon: Pokemon): Single<Pokemon> {

        return if (Common.isConnectedToNetwork(application))
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
                favoritePokemon(it, buttonState)
            }, {
                it.printStackTrace()
            })
    }

    private fun favoritePokemon(pokemon: Pokemon, buttonState: Boolean) {
        mAppDatabase.pokemonDao().setFavorite(pokemon.id, buttonState)
    }


    private fun makePokemonDetailApiCall(pokemon: Pokemon): Single<Pokemon> {
        return RetroFitClient.INSTANCE.getPokemonDetails(pokemon.id)
            .map {
                it.id = pokemon.id
                it.name = pokemon.name
                it.url = pokemon.url
                it.favorite = pokemon.favorite
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

