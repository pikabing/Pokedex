package com.example.pokemon.contract

import com.example.pokemon.model.Pokemon

interface MainContract {

    interface View {
        fun showPokemonRV()
        fun hidePokemonRV()
        fun setPokemonAdapter(pokeList: List<Pokemon>)
        fun resetPokemonList(pokeList: List<Pokemon>)
        fun showErrorToast()
    }

    interface Presenter {
        fun loadMorePokemons()
        fun getPokemon(id: Int): Pokemon
        fun setFavorite(pokemon: Pokemon, buttonState: Boolean)
        fun getPokemonDetailsFromDb()
        fun onDestroy()
        fun takeView(view: View?)
    }
}