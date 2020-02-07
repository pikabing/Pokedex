package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.MvpPresenter
import com.example.pokemon.utils.common.MvpView

interface MainContract {

    interface View: MvpView{
        fun showPokemonRV()
        fun hidePokemonRV()
        fun setPokemonAdapter(pokeList: List<Pokemon>)
        fun setListToAdapter(pokeList: List<Pokemon>)
        fun refreshPokemonList()
        fun returnToTop()

    }

    interface Presenter: MvpPresenter<View> {
        fun loadMorePokemons()
        fun getPokemon(id: Int): Pokemon
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun getPokemonListFromDb()
        fun reloadPokemonList()
    }
}