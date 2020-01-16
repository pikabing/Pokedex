package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon

interface MainContract {

    interface MainView {
        fun showPokemonRV()
        fun hidePokemonRV()
        fun setPokemonAdapter(pokeList: ArrayList<Pokemon>)
        fun showErrorToast()
    }

    interface MainPresenter {
        fun loadMorePokemons(offset: Int)
        fun getPokemonList(id: Int): Pokemon
    }
}