package com.example.pokemon.data.db

import androidx.room.*
import com.example.pokemon.model.Pokemon
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun fetchPokemonList(): Single<List<Pokemon>>

    @Query("SELECT * FROM pokemon WHERE id LIKE :pokemonId")
    fun fetchPokemonDetail(pokemonId: Int): Maybe<Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: List<Pokemon>)

    @Query("SELECT id from pokemon WHERE favorite = 1")
    fun getFavoritePokemonsID(): List<Int>

    @Update
    fun update(pokemon: Pokemon)

    @Query("UPDATE pokemon SET favorite= :value WHERE id = :pokemonId")
    fun setFavorite(pokemonId: Int, value: Boolean)

    @Query("SELECT * FROM pokemon WHERE favorite = 1")
    fun fetchFavoriteList() : Single<List<Pokemon>>
}