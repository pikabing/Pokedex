package com.example.pokemon.presenters

import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoritePresenter
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : BasePresenter<FavoritesContract.View>(),
    FavoritesContract.Presenter {

    private var pokeList: ArrayList<Pokemon> = arrayListOf()

    override fun getFavoriteList() {
        mCompositeDisposable.add(
            pokemonRepository.getFavoritePokemonsList().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                it?.let {
                    pokeList = ArrayList(it)
                    mView?.setPokemonAdapter(pokeList)
                }
            }, {
                mView?.showErrorToast("Error retrieving favorite Pokemon list")
            })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    override fun getPokemon(id: Int) = pokeList[id]

}