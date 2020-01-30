package com.example.pokemon.presenter

import com.example.pokemon.MyApplication
import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonDetailPresenterImpl(private var pokemonDetailView: PokemonDetailContract.PokemonDetailView?) : PokemonDetailContract.PokemonDetailPresenter {

    private val appDatabase = AppDatabase.getAppDataBase(MyApplication.application.applicationContext)
    private val pokemonRepository = PokemonRepository.getInstance(appDatabase)
    private val compositeDisposable = CompositeDisposable()

    override fun getPokemonDetails(pokemon: Pokemon) {
        compositeDisposable.add(pokemonRepository.getPokemonDetails(pokemon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                populateDetails(it)
            }, {
                pokemonDetailView?.showErrorToast()
            })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) = pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    private fun populateDetails(pokemon: Pokemon?) {

        pokemon?.let {
            if(it.height == null) {
                pokemonDetailView?.pokemonDetailsNotCached(it.name)
            } else {
                pokemonDetailView?.setPokemonDetails(it)
                pokemonDetailView?.hideProgressBar()
            }

        }
        pokemonDetailView?.hideProgressBar()

    }

    override fun onDestroy() {
        pokemonDetailView = null
        compositeDisposable.dispose()
    }

}