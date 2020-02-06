package com.example.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.model.Pokemon
import com.example.pokemon.utils.DiffUtilCallBack
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.pokemon_list.view.*


class PokemonAdapter(
    private var pokeList: ArrayList<Pokemon>,
    private var listener: PokemonAdapterListener?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_LOADER = 0
    private val VIEW_LIST = 1
    private var loading: Boolean = false

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
            is PokemonViewHolder -> holder.bind(pokeList[position], listener)
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


        fun bind(pokemon: Pokemon, pokemonAdapterListener: PokemonAdapterListener?) =
            with(itemView) {
                itemView.pokemonCardTitle.text = pokemon.name
                Glide.with(itemView)
                    .load(itemView.resources.getString(R.string.pokemon_image_url) + pokemon.id + ".png")
                    .placeholder(R.drawable.placeholder)
                    .into(itemView.pokemonCardImage)
                itemView.setOnClickListener {
                    pokemonAdapterListener?.cardOnClick(pokemon, adapterPosition)
                }
                itemView.favoriteButtonInList.isChecked = pokemon.favorite
                itemView.favoriteButtonInList.setEventListener(object : SparkEventListener {
                    override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
                    }

                    override fun onEvent(button: ImageView?, buttonState: Boolean) {
                        pokemonAdapterListener?.favoriteButton(pokemon, buttonState)
                        pokemon.favorite = buttonState
                        favoriteButtonInList.playAnimation()
                    }

                    override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
                    }

                })

            }

    }

    class ProgressBarViewHolder(v: View) : RecyclerView.ViewHolder(v)

    fun updateData(pokeList: List<Pokemon>) {
        val diffUtilCallBack = DiffUtilCallBack(this.pokeList, pokeList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        this.pokeList.clear()
        this.pokeList.addAll(pokeList)
        diffResult.dispatchUpdatesTo(this)

    }

    fun removePokemonFromFavorite(pokemon: Pokemon) {
        val index = pokeList.indexOf(pokemon)
        if (index != -1) {
            pokeList.remove(pokemon)
            notifyItemRemoved(index)
        }
    }

    fun setListenerToNull() {
        listener = null
    }

    interface PokemonAdapterListener {
        fun cardOnClick(pokemon: Pokemon, position: Int)
        fun favoriteButton(pokemon: Pokemon, buttonState: Boolean)
    }

}