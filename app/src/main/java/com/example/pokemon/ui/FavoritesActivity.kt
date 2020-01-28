package com.example.pokemon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.FavoritePresenterImpl
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_favorites.backButton

class FavoritesActivity : AppCompatActivity(), FavoritesContract.FavoriteView, PokemonAdapter.PokemonAdapterListener {

    private val pokeList: ArrayList<Pokemon> = arrayListOf()
    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var pokemonAdapter: PokemonAdapter? = null
    private var favoritePresenterImpl : FavoritePresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritePresenterImpl = FavoritePresenterImpl(this)
        pokemonAdapter = PokemonAdapter(pokeList, this)

        favorites_list.layoutManager = layoutManager
        favorites_list.adapter = pokemonAdapter
        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        favorites_list.addItemDecoration(PokemonItemDecoration(sidePadding))

        favoritePresenterImpl?.getFavoriteList()

        backButton.setOnClickListener { finish() }

    }

    override fun setPokemonAdapter(pokeList: List<Pokemon>) {
        pokemonAdapter?.addData(pokeList)
    }

    override fun showErrorToast() {
        Toast.makeText(this, "Error retrieving favorite Pokemon list",Toast.LENGTH_SHORT).show()
    }

    override fun cardOnClick(pokemon: Pokemon) {
        val intent = Intent(this, PokemonDetailActivity::class.java)
        favoritePresenterImpl?.getPokemon(pokemon.id)?.let {
            intent.putExtra("Pokemon", Gson().toJson(it))
            startActivity(intent)
        }
    }

    override fun favoriteButtonOn(pokemon: Pokemon) {
        favoritePresenterImpl?.setFavorite(pokemon, true)
    }

    override fun favoriteButtonOff(pokemon: Pokemon) {
        pokemonAdapter?.removePokemonFromFavorite(pokemon)
        favoritePresenterImpl?.setFavorite(pokemon, false)
    }
}