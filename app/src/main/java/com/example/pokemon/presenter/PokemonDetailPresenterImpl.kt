package com.example.pokemon.presenter

import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.model.PokemonDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailPresenterImpl(private var pokemonDetailView: PokemonDetailContract.PokemonDetailView?) : PokemonDetailContract.PokemonDetailPresenter {

    override fun getPokemonDetails(id: Int) = RetroFitClient.instance.getPokemonDetails(id).enqueue(object:
            Callback<PokemonDetail> {
            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                pokemonDetailView?.showErrorToast()
            }

            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) = populateDetails(response.body())

        })

    private fun populateDetails(pokemonDetail: PokemonDetail?) {

        pokemonDetail?.let {
            pokemonDetailView?.setPokemonDetails(it)
            pokemonDetailView?.hideProgressBar()
        }

    }

    override fun onDestroy() {
        pokemonDetailView = null
    }

}