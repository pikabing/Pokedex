package com.example.pokemon.presenter

import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenterImpl(private var mainView: MainContract.MainView?) : MainContract.MainPresenter {

    private val pokeList: ArrayList<Pokemon> = arrayListOf()
    private var offset: Int = 0

    override fun loadMorePokemons() {

        if(offset > 960)
            return

        callPokemonApi(offset)
        offset+=8

    }

    override fun getPokemonList(id: Int) = pokeList[id]

    override fun onDestroy() {
        mainView = null
    }

    private fun callPokemonApi(offset: Int) = RetroFitClient.instance.getPokemons(offset, 8)
        .enqueue(object: Callback<PokemonResponse> {
            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                mainView?.showErrorToast()
            }

            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ) = populateList(response.body()?.results)
        })


    fun populateList(response: ArrayList<Pokemon>?) {
        response?.let {
            response.map {
                val tokens = it.url.split("/")
                it.id = tokens[tokens.lastIndex - 1]
                it.name = it.name.capitalize()
            }
            pokeList.addAll(response)
            mainView?.setPokemonAdapter(response)
            mainView?.showPokemonRV()
        }

    }
}