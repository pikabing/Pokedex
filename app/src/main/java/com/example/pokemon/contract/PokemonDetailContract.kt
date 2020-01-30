package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon

interface PokemonDetailContract {

    interface PokemonDetailView {

        fun hideProgressBar()
        fun setPokemonDetails(pokemon: Pokemon)
        fun pokemonDetailsNotCached(name: String)
        fun makePokemonFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun showErrorToast()

    }

    interface PokemonDetailPresenter {

        fun getPokemonDetails(pokemon: Pokemon)
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun onDestroy()
    }
}