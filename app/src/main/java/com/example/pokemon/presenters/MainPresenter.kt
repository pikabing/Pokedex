package com.example.pokemon.presenters

import android.annotation.SuppressLint
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.utils.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter
@Inject constructor
    (private val pokemonRepository: PokemonRepository) : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    private var pokeList: ArrayList<Pokemon> = arrayListOf()

    private var offset: Int = 0

    override fun loadMorePokemons() {

        if (offset > 960)
            return

        getPokemonsFromRepo(offset)
        offset += 8

    }

    override fun getPokemonListFromDb() {
        mCompositeDisposable.add(
            pokemonRepository.getPokemonListFromDB().subscribeOn(Schedulers.io())
                .observeOn(
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

    private fun getPokemonsFromRepo(offset: Int) {

        mCompositeDisposable.add(pokemonRepository.getPokemonList(offset)
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    populateList(it)
                }
            }, {
                mView?.showErrorToast("Error calling API")
            }
            ))

    }


    @SuppressLint("DefaultLocale")
    private fun populateList(response: List<Pokemon>) {
        response.let {
            pokeList.addAll(response)
            mView?.setPokemonAdapter(response)
            mView?.showPokemonRV()
        }

    }

    private fun rePopulateList(response: List<Pokemon>) {
        response.let {
            if (it.isNotEmpty()) {
                pokeList = ArrayList(it)
                mView?.resetPokemonList(it)
            }
        }
    }
}