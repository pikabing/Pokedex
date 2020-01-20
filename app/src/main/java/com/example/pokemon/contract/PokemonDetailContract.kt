package com.example.pokemon.contract

import com.example.pokemon.model.PokemonDetail

interface PokemonDetailContract {

    interface PokemonDetailView {

        fun hideProgressBar()
        fun setPokemonDetails(pokemonDetail: PokemonDetail)
        fun showErrorToast()

    }

    interface PokemonDetailPresenter {

        fun getPokemonDetails(id: Int)
        fun onDestroy()
    }
}