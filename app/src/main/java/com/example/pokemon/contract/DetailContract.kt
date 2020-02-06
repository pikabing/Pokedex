package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.MvpPresenter
import com.example.pokemon.utils.common.MvpView

interface DetailContract {

    interface View: MvpView {
        fun hideProgressBar()
        fun setPokemonDetails(pokemon: Pokemon)
        fun pokemonDetailsNotCached(name: String)
        fun makePokemonFavorite(pokemon: Pokemon, buttonState: Boolean)
    }

    interface Presenter: MvpPresenter<View> {
        fun getPokemonDetails(pokemon: Pokemon)
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
    }
}