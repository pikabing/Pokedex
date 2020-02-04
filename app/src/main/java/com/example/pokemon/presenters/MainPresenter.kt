package com.example.pokemon.presenters

import android.annotation.SuppressLint
import com.example.pokemon.contract.MainContract
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.model.Pokemon
import com.example.pokemon.data.repository.PokemonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter
@Inject constructor
    (private val pokemonRepository: PokemonRepository) :
    MainContract.Presenter {

    private var view: MainContract.View? = null

    override fun setView(view: MainContract.View) {
        this.view = view
    }

    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private var offset: Int = 0

    // Can this composite disposable be removed using dagger?

    private val compositeDisposable = CompositeDisposable()

    override fun loadMorePokemons() {

        if (offset > 960)
            return

        getPokemonsFromRepo(offset)
        offset += 8

    }

    override fun getPokemonDetailsFromDb() {
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

    override fun getPokemon(id: Int): Pokemon = pokeList[id]

    override fun setFavorite(pokemon: Pokemon, buttonState: Boolean) =
        pokemonRepository.setFavoritePokemon(pokemon, buttonState)

    override fun onDestroy() {
        view = null
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
                view?.showErrorToast()
            }
            ))

    }


    @SuppressLint("DefaultLocale")
    private fun populateList(response: List<Pokemon>) {
        response.let {
            pokeList.addAll(response)
            view?.setPokemonAdapter(response)
            view?.showPokemonRV()
        }

    }

    private fun rePopulateList(response: List<Pokemon>) {
        response.let {
            pokeList = ArrayList(it)
            view?.resetPokemonList(it)
        }
    }
}