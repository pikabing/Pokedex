package com.example.pokemon.presenters

import com.example.pokemon.contract.DetailContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailPresenter
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : DetailContract.Presenter {

    private var view: DetailContract.View? = null

    private val compositeDisposable = CompositeDisposable()

    override fun getPokemonDetails(pokemon: Pokemon) {
        compositeDisposable.add(
            pokemonRepository.getPokemonDetails(pokemon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    populateDetails(it)
                }, {
                    view?.showErrorToast()
                })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    private fun populateDetails(pokemon: Pokemon?) {

        pokemon?.let {
            if (it.height == null)
                view?.pokemonDetailsNotCached(it.name)
            else
                view?.setPokemonDetails(it)
        }
        view?.hideProgressBar()

    }

    override fun takeView(view: DetailContract.View?) {
        this.view = view
    }

    override fun onDestroy() {
        view = null
        compositeDisposable.dispose()
    }

}