package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon

interface PokemonDetailContract {

    interface PokemonDetailView {

        fun hideProgressBar()
        fun setPokemonDetails(pokemon: Pokemon)
        fun showErrorToast()

    }

    interface PokemonDetailPresenter {

        fun getPokemonDetails(id: Int)
        fun onDestroy()
    }
}