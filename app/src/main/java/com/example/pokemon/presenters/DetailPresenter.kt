package com.example.pokemon.presenters

import com.example.pokemon.contract.DetailContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailPresenter
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : BasePresenter<DetailContract.View>() , DetailContract.Presenter {

    override fun getPokemonDetails(pokemon: Pokemon) {
        mCompositeDisposable.add(
            pokemonRepository.getPokemonDetails(pokemon)
                .retry(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    populateDetails(it)
                }, {
                    mView?.showErrorToast("Failed to get details")
                    mView?.hideProgressBar()
                })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    private fun populateDetails(pokemon: Pokemon?) {

        pokemon?.let {
            if (it.height == null)
                mView?.pokemonDetailsNotCached(it.name)
            else
                mView?.setPokemonDetails(it)
        }
        mView?.hideProgressBar()

    }

}