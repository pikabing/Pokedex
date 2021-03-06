package com.example.pokemon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.contract.DetailContract
import com.example.pokemon.model.Pokemon
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.varunest.sparkbutton.SparkEventListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_pokemon_detail.*
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(),
    DetailContract.View {

    @Inject
    lateinit var detailPresenter: DetailContract.Presenter

    private var pokemon: Pokemon? = null

    override val viewContext: Context? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        detailPresenter.takeView(this)

        backButton.setOnClickListener { finish() }

        val pokemonString = intent.extras?.getString("Pokemon")

        if (pokemonString != null) {
            pokemon = Gson().fromJson(pokemonString, Pokemon::class.java)
        }

        pokemon?.let {
            pokemonDetailHeading.text = it.name
            nameOfPokemon.text = it.name
            Glide.with(this)
                .load(this.resources.getString(R.string.pokemon_image_url) + it.id + ".png")
                .placeholder(R.drawable.placeholder)
                .into(pokemonDetailImage)
            favoriteButtonInDetail.isChecked = it.favorite
            detailPresenter.getPokemonDetails(it)
        }

        favoriteButtonInDetail.setEventListener(object : SparkEventListener {
            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {

            }

            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (pokemon?.height != null)
                    detailPresenter.setFavorite(pokemon!!, buttonState)
                else {
                    favoriteButtonInDetail.isChecked = false
                    favoriteButtonInDetail.playAnimation()
                    Toast.makeText(applicationContext, "Please let ${pokemon?.name}'s details load...", Toast.LENGTH_SHORT).show()
                }
                if(!buttonState) favoriteButtonInDetail.playAnimation()
            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
            }

        })

    }


    override fun hideProgressBar() {
        pokemonDetailCard.visibility = View.VISIBLE
        pokemonDetailSpinner.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun setPokemonDetails(pokemon: Pokemon) {

        this.pokemon = pokemon

        heightOfPokemon.text = "Height: ${pokemon.height}m"

        weightOfPokemon.text = "Weight: ${pokemon.weight}g"

        experienceOfPokemon.text = "Points: ${pokemon.base_experience}"

        movesofPokemon.text = "Moves: ${pokemon.moves?.size()}"

        pokemon.types?.forEach {
            MaterialButton(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = it.type.name
                val param = layoutParams as LinearLayout.LayoutParams
                param.setMargins(0, 5, 20, 5)
                layoutParams = param
                cornerRadius = 15

                backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
                typesOfPokemonDetails.addView(this)
            }

        }

        pokemon.abilities?.forEach {
            MaterialButton(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = it.ability.name
                val param = layoutParams as LinearLayout.LayoutParams
                param.setMargins(0, 5, 20, 0)
                layoutParams = param
                cornerRadius = 15
                textSize = 12.0F
                backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
                abilityOfPokemonDetails.addView(this)
            }

        }


    }

    @SuppressLint("DefaultLocale")
    override fun pokemonDetailsNotCached(name: String) {
        detailCLayout.visibility = View.GONE
        pokemonDetailSpinner.visibility = View.GONE
        val myToast = Toast.makeText(
            applicationContext,
            "${name.capitalize()}'s details couldn't be cached",
            Toast.LENGTH_SHORT
        )
        myToast.setGravity(0, 0, 0)
        myToast.show()
    }

    override fun makePokemonFavorite(pokemon: Pokemon, buttonState: Boolean) {
        detailPresenter.setFavorite(pokemon, buttonState)
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.dropView()
    }

}