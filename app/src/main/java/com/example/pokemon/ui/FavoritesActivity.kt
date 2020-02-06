package com.example.pokemon.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.PokemonItemDecoration
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_favorites.backButton
import javax.inject.Inject

class FavoritesActivity : DaggerAppCompatActivity(),
    FavoritesContract.View,
    PokemonAdapter.PokemonAdapterListener {

    private val pokeList: ArrayList<Pokemon> = arrayListOf()

    private val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    private var pokemonAdapter: PokemonAdapter? = null

    @Inject
    lateinit var favoritePresenter: FavoritesContract.Presenter

    override val viewContext: Context? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritePresenter.takeView(this)

        pokemonAdapter = PokemonAdapter(pokeList, this)

        favorites_list.layoutManager = layoutManager
        favorites_list.adapter = pokemonAdapter

        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        favorites_list.addItemDecoration(PokemonItemDecoration(sidePadding))

        backButton.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()
        favoritePresenter.getFavoriteList()
    }

    override fun setPokemonAdapter(pokeList: List<Pokemon>) {
        pokemonAdapter?.updateData(pokeList)
    }

    override fun cardOnClick(pokemon: Pokemon, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Pokemon", Gson().toJson(pokemon))
        startActivity(intent)
    }

    override fun favoriteButton(pokemon: Pokemon, buttonState: Boolean) {
        favoritePresenter.setFavorite(pokemon, buttonState)
        if (!buttonState) pokemonAdapter?.removePokemonFromFavorite(pokemon)

    }

}