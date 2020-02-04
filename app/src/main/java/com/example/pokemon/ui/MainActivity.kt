package com.example.pokemon.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.MyApplication
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() ,
    MainContract.View,
    PokemonAdapter.PokemonAdapterListener {

    private val isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private val pokeList: ArrayList<Pokemon> = arrayListOf()
    private var pokemonAdapter: PokemonAdapter? = null

    @Inject
    lateinit var application: MyApplication
    @Inject
    lateinit var presenter:MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokemonAdapter = PokemonAdapter(pokeList, this)
        hidePokemonRV()
        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        pokemonAdapter?.handleLoading(true)
        presenter.loadMorePokemons()

        pokemonRV.addOnScrollListener(object : PagingListener(layoutManager, application) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                pokemonAdapter?.handleLoading(true)
                presenter?.loadMorePokemons()
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
        presenter?.getPokemonDetailsFromDb()
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
        presenter?.onDestroy()
    }

    override fun cardOnClick(pokemon: Pokemon, position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("Pokemon", Gson().toJson(pokemon))
        startActivity(intent)
    }

    override fun favoriteButton(pokemon: Pokemon, buttonState: Boolean) {
        presenter?.setFavorite(pokemon, buttonState)
    }

}
