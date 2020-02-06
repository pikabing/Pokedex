package com.example.pokemon.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.common.NetworkCheck
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(),
    MainContract.View,
    PokemonAdapter.PokemonAdapterListener {

    private val isLastPage: Boolean = false

    private var isLoading: Boolean = false

    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    private val pokeList: ArrayList<Pokemon> = arrayListOf()

    private var pokemonAdapter: PokemonAdapter? = null

    private var firstTimeOpened: Boolean = false

    @Inject
    lateinit var presenter: MainContract.Presenter

    override val viewContext: Context? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.takeView(this)

        hidePokemonRV()

        pokemonAdapter = PokemonAdapter(pokeList, this)
        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        pokemonAdapter?.handleLoading(true)
        presenter.loadMorePokemons()

        pokemonRV.addOnScrollListener(object : PagingListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun isConnected(): Boolean = isConnectedToNetwork()

            override fun loadMoreItems() {
                isLoading = true
                pokemonAdapter?.handleLoading(true)
                presenter.loadMorePokemons()
            }

        })

        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        pokemonRV.addItemDecoration(PokemonItemDecoration(sidePadding))

        //open favorites activity
        favorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)

        }

    }

    private fun isConnectedToNetwork() = NetworkCheck.isConnectedToNetwork(applicationContext)

    override fun onResume() {
        super.onResume()

        if(!isConnectedToNetwork() || firstTimeOpened)
            presenter.getPokemonDetailsFromDb()

        firstTimeOpened = true
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

    override fun onDestroy() {
        super.onDestroy()
        firstTimeOpened = false
        pokemonAdapter?.setListenerToNull()
        presenter.dropView()
    }

    override fun cardOnClick(pokemon: Pokemon, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Pokemon", Gson().toJson(pokemon))
        startActivity(intent)
    }

    override fun favoriteButton(pokemon: Pokemon, buttonState: Boolean) {
        presenter.setFavorite(pokemon, buttonState)
    }

}
