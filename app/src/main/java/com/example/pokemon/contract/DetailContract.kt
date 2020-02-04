package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon

interface DetailContract {

    interface View {

        fun hideProgressBar()
        fun setPokemonDetails(pokemon: Pokemon)
        fun pokemonDetailsNotCached(name: String)
        fun makePokemonFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun showErrorToast()

    }

    interface Presenter {

        fun getPokemonDetails(pokemon: Pokemon)
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun onDestroy()
    }
}