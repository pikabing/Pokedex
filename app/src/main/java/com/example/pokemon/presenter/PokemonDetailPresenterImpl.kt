package com.example.pokemon.presenter

import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonDetailPresenterImpl(private var pokemonDetailView: PokemonDetailContract.PokemonDetailView?) : PokemonDetailContract.PokemonDetailPresenter {

    private val pokemonRepository = PokemonRepository.instance
    private val compositeDisposable = CompositeDisposable()

    override fun getPokemonDetails(id: Int) {
        compositeDisposable.add(pokemonRepository.getPokemonDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                populateDetails(it)
            }, {
                pokemonDetailView?.showErrorToast()
            })
        )
    }

    private fun populateDetails(pokemon: Pokemon?) {

        pokemon?.let {
            pokemonDetailView?.setPokemonDetails(it)
            pokemonDetailView?.hideProgressBar()
        }

    }

    override fun onDestroy() {
        pokemonDetailView = null
        compositeDisposable.dispose()
    }

}