package com.example.pokemon.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.PokemonDetailPresenterImpl
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.pokemon_detail.*

class PokemonDetailActivity : AppCompatActivity(), PokemonDetailContract.PokemonDetailView {

    private var pokemonDetailPresenterImpl: PokemonDetailPresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_detail)

        pokemonDetailPresenterImpl = PokemonDetailPresenterImpl(this)

        backButton.setOnClickListener { finish() }

        val pokemonString = intent.extras?.getString("Pokemon")
        var pokemon: Pokemon? = null

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

            pokemonDetailPresenterImpl?.getPokemonDetails(it)
        }

        favoriteButtonInDetail.setEventListener(object : SparkEventListener {
            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {

            }

            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (pokemon != null)
                    pokemonDetailPresenterImpl?.setFavorite(pokemon, buttonState)
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

    override fun setPokemonDetails(pokemon: Pokemon) {

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

    }

    override fun showErrorToast() {
        Toast.makeText(
            this@PokemonDetailActivity,
            "Failed to get details",
            Toast.LENGTH_LONG
        ).show()
        hideProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        pokemonDetailPresenterImpl?.onDestroy()
    }

}