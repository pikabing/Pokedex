package com.example.pokemon.presenter

import android.annotation.SuppressLint
import com.example.pokemon.MyApplication
import com.example.pokemon.contract.MainContract
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.model.Pokemon
import com.example.pokemon.data.repository.PokemonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainPresenterImpl(private var mainView: MainContract.MainView?) : MainContract.MainPresenter {

    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private var offset: Int = 0
    private val appDatabase =
        AppDatabase.getAppDataBase(MyApplication.application.applicationContext)
    private val pokemonRepository = PokemonRepository.getInstance(appDatabase)
    private val compositeDisposable = CompositeDisposable()

    override fun loadMorePokemons() {

        if (offset > 960)
            return

        getPokemonsFromRepo(offset)
        offset += 8

    }

    override fun getPokemonDetailsFromDb(){
        compositeDisposable.add(
            pokemonRepository.getPokemonListFromDB().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                rePopulateList(it)
            }, {
                it.printStackTrace()
            })
        )
    }

    override fun getPokemon(pos: Int): Pokemon = pokeList[pos]

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    override fun onDestroy() {
        mainView = null
        compositeDisposable.dispose()
        AppDatabase.destroyDataBase()
    }

    private fun getPokemonsFromRepo(offset: Int) {

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
    private fun populateList(response: List<Pokemon>) {
        response.let {
            pokeList.addAll(response)
            mainView?.setPokemonAdapter(response)
            mainView?.showPokemonRV()
        }

    }

    private fun rePopulateList(response: List<Pokemon>) {
        response.let {
            pokeList = ArrayList(it)
            mainView?.resetPokemonList(it)
        }
    }
}