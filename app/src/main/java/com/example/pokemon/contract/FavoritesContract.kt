package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.MvpPresenter
import com.example.pokemon.utils.common.MvpView


interface FavoritesContract {

    interface View: MvpView {
        fun setPokemonAdapter(pokeList: List<Pokemon>)
    }

    interface Presenter: MvpPresenter<View> {
        fun getFavoriteList()
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun getPokemon(id: Int) : Pokemon
    }
}