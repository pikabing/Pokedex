package com.example.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.model.Pokemon
import kotlinx.android.synthetic.main.pokemon_list.view.*

class PokemonAdapter(var pokeList: ArrayList<Pokemon>, val listener: (Int) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list, parent, false)
    )


    override fun getItemCount(): Int = pokeList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(pokeList[position], position, listener)


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pokemon: Pokemon, pos: Int, listener: (Int) -> Unit) = with(itemView) {
            itemView.pokemonCardTitle.text = pokemon.name
            Glide.with(itemView)
                .load(itemView.resources.getString(R.string.pokemon_image_url) + pokemon.id + ".png")
                .into(itemView.pokemonCardImage)
            itemView.setOnClickListener {
                listener.invoke(pos)
            }
        }


    }

    fun addData(pokeList: ArrayList<Pokemon>) {
        var size = this.pokeList.size
        this.pokeList = pokeList
        var sizeNew = pokeList.size
        notifyItemRangeChanged(size, sizeNew)
    }

}