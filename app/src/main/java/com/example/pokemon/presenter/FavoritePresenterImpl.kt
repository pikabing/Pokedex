package com.example.pokemon.presenter

import com.example.pokemon.MyApplication
import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritePresenterImpl(private var favoriteView: FavoritesContract.FavoriteView?) :
    FavoritesContract.FavoritePresenter {

    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private val appDatabase = AppDatabase.getAppDataBase(MyApplication.application.applicationContext)
    private val pokemonRepository = PokemonRepository.getInstance(appDatabase)
    private val compositeDisposable = CompositeDisposable()

    override fun getFavoriteList() {
        compositeDisposable.add(
            pokemonRepository.getFavoritePokemonsList().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                it?.let {
                    pokeList = ArrayList(it)
                    favoriteView?.setPokemonAdapter(pokeList)
                }
            }, {
                favoriteView?.showErrorToast()
            })
        )
    }

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) = pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    override fun getPokemon(id: Int) = pokeList[id]

    override fun onDestroy() {
        favoriteView = null
        compositeDisposable.dispose()
    }

}