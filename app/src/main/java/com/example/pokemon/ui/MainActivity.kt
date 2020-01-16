package com.example.pokemon.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.MainPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.MainView {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var layoutManager = GridLayoutManager(this, 2)
    private var pokeList: ArrayList<Pokemon> = arrayListOf()
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var mainPresenterImpl: MainPresenterImpl

    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hidePokemonRV()

        mainPresenterImpl = MainPresenterImpl(this)

        pokemonAdapter = PokemonAdapter(pokeList) {
            var intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
            intent.putExtra("Pokemon", mainPresenterImpl.getPokemonList(it))
            startActivity(intent)
        }

        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        mainPresenterImpl.loadMorePokemons(offset)

        pokemonRV.addOnScrollListener(object: PagingListener(layoutManager){
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                offset+=8
                mainPresenterImpl.loadMorePokemons(offset)
            }

        })

    }

    override fun showPokemonRV() {
        pokemonRV.visibility = View.VISIBLE
        mainScreenLoader.visibility = View.GONE
    }

    override fun hidePokemonRV() {
        pokemonRV.visibility = View.GONE
    }

    override fun setPokemonAdapter(pokeList: ArrayList<Pokemon>) {
        isLoading = false
        pokemonAdapter.addData(pokeList)
    }

    override fun showErrorToast() {
        Toast.makeText(this, "Error calling API", Toast.LENGTH_LONG).show()
    }
}
