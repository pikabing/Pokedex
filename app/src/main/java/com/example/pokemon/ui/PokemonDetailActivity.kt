package com.example.pokemon.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.presenter.PokemonDetailPresenterImpl
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.pokemon_detail.*

class PokemonDetailActivity : AppCompatActivity(), PokemonDetailContract.PokemonDetailView {

    private var pokemonDetailPresenterImpl: PokemonDetailPresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_detail)

        pokemonDetailPresenterImpl = PokemonDetailPresenterImpl(this)

        backButton.setOnClickListener { finish() }

        val pokemon = intent.getSerializableExtra("Pokemon") as? Pokemon
        pokemon?.let {
            pokemonDetailHeading.text = it.name
            nameOfPokemon.text = it.name
            Glide.with(this)
                .load(this.resources.getString(R.string.pokemon_image_url) + it.id + ".png")
                .into(pokemonDetailImage)

            pokemonDetailPresenterImpl?.getPokemonDetails(it.id.toInt())
        }

    }


    override fun hideProgressBar() {
        pokemonDetailCard.visibility = View.VISIBLE
        pokemonDetailSpinner.visibility = View.GONE
    }

    override fun setPokemonDetails(response: Pokemon) {

        heightOfPokemon.text = "Height: ${response.height}m"

        weightOfPokemon.text = "Weight: ${response.weight}g"

        experienceOfPokemon.text = "Points: ${response.base_experience}"

        movesofPokemon.text = "Moves: ${response.moves?.size()}"

        response.types?.forEach {
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

        response.abilities?.forEach {
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

    override fun showErrorToast() = Toast.makeText(
        this@PokemonDetailActivity,
        "Failed to get details",
        Toast.LENGTH_LONG
    ).show()

    override fun onDestroy() {
        super.onDestroy()
        pokemonDetailPresenterImpl?.onDestroy()
    }

}