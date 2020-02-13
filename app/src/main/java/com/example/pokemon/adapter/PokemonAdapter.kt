package com.example.pokemon.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private val VIEW_LOADER = 0
    private val VIEW_LIST = 1
    private var loading: Boolean = false
    private var filteredPokeList: ArrayList<Pokemon> = this.pokeList

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

    override fun getItemCount(): Int = if (loading) filteredPokeList.size + 1 else filteredPokeList.size

    override fun getItemViewType(position: Int): Int {
        return when (loading && position == itemCount - 1) {
            false -> VIEW_LIST
            else -> VIEW_LOADER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonViewHolder -> holder.bind(filteredPokeList[position], listener)
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
            notifyItemInserted(filteredPokeList.size)
        } else {
            notifyItemRemoved(filteredPokeList.size)
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
        Log.e("updateData", "" + pokeList.size + " - " + this.pokeList.size + " - " + filteredPokeList.size)
        val diffUtilCallBack = DiffUtilCallBack(this.pokeList, pokeList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        this.pokeList = ArrayList(pokeList)
        filteredPokeList = ArrayList(pokeList)

//        filteredPokeList.clear()
//        filteredPokeList.addAll(pokeList)

        diffResult.dispatchUpdatesTo(this)

    }

    fun removePokemonFromFavorite(pokemon: Pokemon) {
        val index = filteredPokeList.indexOf(pokemon)
        if (index != -1) {
            filteredPokeList.remove(pokemon)
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

    override fun getFilter(): Filter = object : Filter() {

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val charString = charSequence.toString()
            filteredPokeList = if (charString.isEmpty()) ArrayList(pokeList)
            else {
                val filteredList: ArrayList<Pokemon> = ArrayList()

                for (pokemon in pokeList) {
                    if (pokemon.name.contains(charString.toLowerCase()) || pokemon.name.contains(charString.capitalize()))
                        filteredList.add(pokemon)
                }
                filteredList
            }

            val filterResults = FilterResults()
            filterResults.values = filteredPokeList
            Log.e("performFiltering", "" + filteredPokeList.size)
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            Log.e("TAG", "${(filterResults.values as ArrayList<Pokemon>).size}")
//            notifyDataSetChanged()
            updateData(filterResults.values as List<Pokemon>)
        }
    }

}