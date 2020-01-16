package com.example.pokemon.api

import com.example.pokemon.model.PokemonDetail
import com.example.pokemon.model.PokemonResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("pokemon")
    fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(
        @Path("id") id: Int
    ): Call<PokemonDetail>
}