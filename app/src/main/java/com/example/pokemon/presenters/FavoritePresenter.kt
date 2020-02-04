package com.example.pokemon.presenters

import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoritePresenter
@Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private var view: FavoritesContract.View?
) :
    FavoritesContract.Presenter {

    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private val compositeDisposable = CompositeDisposable()

    override fun getFavoriteList() {
        compositeDisposable.add(
            pokemonRepository.getFavoritePokemonsList().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                it?.let {
                    pokeList = ArrayList(it)
                    view?.setPokemonAdapter(pokeList)
                }
            }, {
                view?.showErrorToast()
            })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    override fun getPokemon(id: Int) = pokeList[id]

    override fun onDestroy() {
        view = null
        compositeDisposable.dispose()
    }

}