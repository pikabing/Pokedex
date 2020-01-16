package com.example.pokemon.presenter

import android.util.Log
import android.widget.Toast
import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class MainPresenterImpl(var mainView: MainContract.MainView) : MainContract.MainPresenter {

    var pokeList: ArrayList<Pokemon> = arrayListOf()

    override fun loadMorePokemons(offset: Int) {

        if(offset <= 960)
            callPokemonApi(offset)


    }

    override fun getPokemonList(id: Int) = pokeList[id]

    fun callPokemonApi(offset: Int) = RetroFitClient.instance.getPokemons(offset, 8)
        .enqueue(object: Callback<PokemonResponse> {
            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                mainView.showErrorToast()
            }

            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ) = populateList(response.body()!!.results)
        })

    fun populateList(response: ArrayList<Pokemon>) {
        response.map {
            var tokens = it.url.split("/")
            it.id = tokens[tokens.lastIndex - 1]
            it.name = it.name.capitalize()
        }
        pokeList.addAll(response)
        mainView.setPokemonAdapter(pokeList)
        mainView.showPokemonRV()

    }
}