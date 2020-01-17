package com.example.pokemon.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.MainPresenterImpl
import com.example.pokemon.utils.PokemonItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.MainView {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var mainPresenterImpl: MainPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenterImpl = MainPresenterImpl(this)
        pokemonAdapter = PokemonAdapter(pokeList) {
            val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
            intent.putExtra("Pokemon", mainPresenterImpl.getPokemonList(it))
            startActivity(intent)
        }

        hidePokemonRV()
        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        pokemonAdapter.handleLoading(true)
        mainPresenterImpl.loadMorePokemons()

        pokemonRV.addOnScrollListener(object: PagingListener(layoutManager){
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                pokemonAdapter.handleLoading(true)
                mainPresenterImpl.loadMorePokemons()
            }

        })

        var sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        pokemonRV.addItemDecoration(PokemonItemDecoration(sidePadding))

    }

    override fun showPokemonRV() {
        pokemonRV.visibility = View.VISIBLE
        mainScreenLoader.visibility = View.GONE
    }

    override fun hidePokemonRV() {
        pokemonRV.visibility = View.GONE
    }

    override fun setPokemonAdapter(pokeList: ArrayList<Pokemon>) {
        pokemonAdapter.handleLoading(false)
        pokemonAdapter.addData(pokeList)
        isLoading = false

    }

    override fun showErrorToast() {
        Toast.makeText(this, "Error calling API", Toast.LENGTH_LONG).show()
    }


}
