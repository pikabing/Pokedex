package com.example.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.model.Pokemon
import kotlinx.android.synthetic.main.pokemon_list.view.*


class PokemonAdapter(var pokeList: ArrayList<Pokemon>, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_LOADER = 0
    val VIEW_LIST = 1
    var loading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_LOADER -> ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.progress_bar,
                    parent,
                    false
                )
            )

            VIEW_LIST -> PokemonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.pokemon_list,
                    parent,
                    false
                )
            )

            else -> ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.progress_bar,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = if (loading) pokeList.size + 1 else pokeList.size

    override fun getItemViewType(position: Int): Int {
        return when (loading && position == itemCount - 1) {
            false -> VIEW_LIST
            else -> VIEW_LOADER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonViewHolder -> holder.bind(pokeList[position], position, listener)
            is ProgressBarViewHolder -> {
                val layoutParams = holder.itemView.layoutParams
                if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    layoutParams.isFullSpan = true
                }
            }
        }
    }

    fun handleLoading(loading: Boolean) {
        this.loading = loading
        if (loading) {
            notifyItemInserted(pokeList.size)
        } else {
            notifyItemRemoved(pokeList.size)
        }
    }


    class PokemonViewHolder(v: View) : RecyclerView.ViewHolder(v) {

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

    class ProgressBarViewHolder(v: View) : RecyclerView.ViewHolder(v)

    fun addData(pokeList: ArrayList<Pokemon>) {
        val size = this.pokeList.size
        this.pokeList.addAll(pokeList)
        val sizeNew = this.pokeList.size
        notifyItemRangeInserted(size, sizeNew)
    }


}