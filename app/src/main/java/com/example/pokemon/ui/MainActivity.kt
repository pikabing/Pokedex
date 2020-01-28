package com.example.pokemon.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.MainPresenterImpl
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.MainView,
    PokemonAdapter.PokemonAdapterListener {

    private val isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private val pokeList: ArrayList<Pokemon> = arrayListOf()
    private var pokemonAdapter: PokemonAdapter? = null
    private var mainPresenterImpl: MainPresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenterImpl = MainPresenterImpl(this)
        pokemonAdapter = PokemonAdapter(pokeList, this)
        hidePokemonRV()
        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        pokemonAdapter?.handleLoading(true)
        mainPresenterImpl?.loadMorePokemons()

        pokemonRV.addOnScrollListener(object : PagingListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                pokemonAdapter?.handleLoading(true)
                mainPresenterImpl?.loadMorePokemons()
            }

        })

        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        pokemonRV.addItemDecoration(PokemonItemDecoration(sidePadding))

        //open favorites activity
        favorites.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()
        mainPresenterImpl?.getPokemonDetailsFromDb()
    }

    override fun showPokemonRV() {
        pokemonRV.visibility = View.VISIBLE
        mainScreenLoader.visibility = View.GONE
    }

    override fun hidePokemonRV() {
        pokemonRV.visibility = View.GONE
    }

    override fun setPokemonAdapter(pokeList: List<Pokemon>) {
        pokemonAdapter?.handleLoading(false)
        pokemonAdapter?.addData(pokeList)
        isLoading = false

    }

    override fun resetPokemonList(pokeList: List<Pokemon>) {
        pokemonAdapter?.updateData(pokeList)
    }

    override fun showErrorToast() {
        Toast.makeText(this, "Error calling API", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        pokemonAdapter?.setListenerToNull()
        mainPresenterImpl?.onDestroy()
    }

    override fun cardOnClick(pokemon: Pokemon, position: Int) {
        val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
        mainPresenterImpl?.getPokemon(position)?.let {
            intent.putExtra("Pokemon", Gson().toJson(it))
            startActivity(intent)
        }
    }

    override fun favoriteButtonOn(pokemon: Pokemon) {
        mainPresenterImpl?.setFavorite(pokemon, true)
    }

    override fun favoriteButtonOff(pokemon: Pokemon) {
        mainPresenterImpl?.setFavorite(pokemon, false)
    }


}
