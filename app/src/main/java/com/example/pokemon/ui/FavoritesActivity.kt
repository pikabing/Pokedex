package com.example.pokemon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenters.FavoritePresenter
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_favorites.backButton

class FavoritesActivity : AppCompatActivity(), FavoritesContract.View,
    PokemonAdapter.PokemonAdapterListener {

    private val pokeList: ArrayList<Pokemon> = arrayListOf()
    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var pokemonAdapter: PokemonAdapter? = null
    private var favoritePresenter: FavoritePresenter? = null
    private lateinit var pokemonRepository: PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritePresenter = FavoritePresenter(pokemonRepository, this)
        pokemonAdapter = PokemonAdapter(pokeList, this)

        favorites_list.layoutManager = layoutManager
        favorites_list.adapter = pokemonAdapter
        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        favorites_list.addItemDecoration(PokemonItemDecoration(sidePadding))

        backButton.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()
        favoritePresenter?.getFavoriteList()
    }

    override fun setPokemonAdapter(pokeList: List<Pokemon>) {
        pokemonAdapter?.updateData(pokeList)
    }

    override fun showErrorToast() {
        Toast.makeText(this, "Error retrieving favorite Pokemon list", Toast.LENGTH_SHORT).show()
    }

    override fun cardOnClick(pokemon: Pokemon, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Pokemon", Gson().toJson(pokemon))
        startActivity(intent)
    }

    override fun favoriteButton(pokemon: Pokemon, buttonState: Boolean) {
        favoritePresenter?.setFavorite(pokemon, buttonState)
        if (!buttonState) pokemonAdapter?.removePokemonFromFavorite(pokemon)

    }

}