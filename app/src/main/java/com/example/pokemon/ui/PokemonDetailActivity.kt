package com.example.pokemon.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.api.RetroFitClient
import com.example.pokemon.contract.PokemonDetailContract
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonDetail
import com.example.pokemon.presenter.PokemonDetailPresenterImpl
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.pokemon_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailActivity : AppCompatActivity(), PokemonDetailContract.PokemonDetailView {

    lateinit var pokemonDetailPresenterImpl: PokemonDetailPresenterImpl

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_detail)

        pokemonDetailPresenterImpl = PokemonDetailPresenterImpl(this)

        val pokemon = intent.getParcelableExtra<Pokemon>("Pokemon")
        val id = pokemon.id.toInt()
        val name = pokemon.name

        backButton.setOnClickListener { finish() }

        pokemonDetailHeading.text = name
        nameOfPokemon.text = name
        Glide.with(this)
            .load(this.resources.getString(R.string.pokemon_image_url) + pokemon.id + ".png")
            .into(pokemonDetailImage)

        pokemonDetailPresenterImpl.getPokemonDetails(id)

    }


    override fun hideProgressBar() {
        pokemonDetailCard.visibility = View.VISIBLE
        pokemonDetailSpinner.visibility = View.GONE
    }

    override fun setPokemonDetails(response: PokemonDetail) {

        heightOfPokemon.text = "Height: ${response.height}m"

        weightOfPokemon.text = "Weight: ${response.weight}g"

        experienceOfPokemon.text = "Points: ${response.base_experience}"

        movesofPokemon.text = "Moves: ${response.moves.size()}"

        var types = response.types
        for (elem in types) {
            var button = MaterialButton(this)
            button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.text = elem.type.name
            val param = button.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,5,20,5)
            button.layoutParams = param
            button.cornerRadius = 15

            button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
            typesOfPokemonDetails.addView(button)
        }

        var abilities = response.abilities
        for(elem in abilities) {
            var button = MaterialButton(this)
            button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.text = elem.ability.name
            val param = button.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,5,20,0)
            button.layoutParams = param
            button.cornerRadius = 15
            button.textSize = 12.0F
            button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
            abilityOfPokemonDetails.addView(button)
        }

    }

    override fun showErrorToast() = Toast.makeText(this@PokemonDetailActivity, "Failed to get details", Toast.LENGTH_LONG).show()

}