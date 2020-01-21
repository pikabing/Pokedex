package com.example.pokemon.presenter

import android.annotation.SuppressLint
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.repository.PokemonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainPresenterImpl(private var mainView: MainContract.MainView?) : MainContract.MainPresenter {

    private var pokeList: ArrayList<Pokemon>? = arrayListOf()
    private var offset: Int = 0
    private val pokemonRepository = PokemonRepository()

    private val compositeDisposable = CompositeDisposable()

    override fun loadMorePokemons() {

        if (offset > 960)
            return

        callPokemonApi(offset)
        offset += 8

    }

    override fun getPokemon(id: Int) = pokeList?.get(id)

    override fun onDestroy() {
        mainView = null
        compositeDisposable.dispose()
    }

    private fun callPokemonApi(offset: Int) {

        compositeDisposable.add(pokemonRepository.getPokemonList(offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    populateList(it)
                }
            }, {
                mainView?.showErrorToast()
            }
            ))

    }


    @SuppressLint("DefaultLocale")
    private fun populateList(response: ArrayList<Pokemon>?) {
        response?.let {
            response.map {
                val tokens = it.url.split("/")
                it.id = tokens[tokens.lastIndex - 1]
                it.name = it.name.capitalize()
            }
            pokeList?.addAll(response)
            mainView?.setPokemonAdapter(response)
            mainView?.showPokemonRV()
        }

    }
}