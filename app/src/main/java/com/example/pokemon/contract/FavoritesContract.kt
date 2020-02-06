package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon


interface FavoritesContract {

    interface View {
        fun setPokemonAdapter(pokeList: List<Pokemon>)
        fun showErrorToast()
    }

    interface Presenter {
        fun getFavoriteList()
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun getPokemon(id: Int) : Pokemon
        fun onDestroy()
        fun takeView(view: View?)
    }
}