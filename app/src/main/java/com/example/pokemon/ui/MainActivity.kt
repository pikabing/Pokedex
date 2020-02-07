package com.example.pokemon.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemon.R
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.PagingListener
import com.example.pokemon.utils.PokemonItemDecoration
import com.example.pokemon.utils.common.NetworkCheck
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

    private lateinit var  searchView: SearchView

    @Inject
    lateinit var presenter: MainContract.Presenter

    override val viewContext: Context? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar stuff
        setSupportActionBar(materialToolbar)
        supportActionBar?.setTitle(R.string.app_name)

        //Set view to presenter
        presenter.takeView(this)

        hidePokemonRV()

        pokemonAdapter = PokemonAdapter(pokeList, this)
        pokemonRV.layoutManager = layoutManager
        pokemonRV.adapter = pokemonAdapter

        pokemonAdapter?.handleLoading(true)
        presenter.loadMorePokemons()

        returnToTop.setOnClickListener {
            returnToTop()
        }

        pokemonRV.addOnScrollListener(object : PagingListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun isConnected(): Boolean = isConnectedToNetwork()

            override fun loadMoreItems() {
                isLoading = true
                pokemonAdapter?.handleLoading(true)
                presenter.loadMorePokemons()
            }

            override fun showReturnToTop() {
                returnToTop.visibility = View.VISIBLE
            }

            override fun hideReturnToTop() {
                returnToTop.visibility = View.GONE
            }

        })

        val sidePadding = resources.getDimensionPixelSize(R.dimen.sidePadding)
        pokemonRV.addItemDecoration(PokemonItemDecoration(sidePadding))

        swipeRefreshLayout.setOnRefreshListener {
            refreshPokemonList()
        }

        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimaryDark))

    }

    private fun isConnectedToNetwork() = NetworkCheck.isConnectedToNetwork(applicationContext)

    override fun onResume() {
        super.onResume()
        presenter.getPokemonListFromDb()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//            }
//
//        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_search) {
            return true
        }

        if (id == R.id.open_favorites) {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
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
        setListToAdapter(pokeList)
        isLoading = false

    }

    override fun setListToAdapter(pokeList: List<Pokemon>) {
        pokemonAdapter?.updateData(pokeList)
    }

    override fun onDestroy() {
        super.onDestroy()
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

    override fun refreshPokemonList() {
        presenter.reloadPokemonList()
    }

    override fun setRefreshFalse() {
        swipeRefreshLayout.isRefreshing = false
    }


    override fun returnToTop() {
        pokemonRV.smoothScrollToPosition(0)
    }


}
