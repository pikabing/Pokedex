package com.example.pokemon.api

import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(
        @Path("id") id: Int
    ): Single<Pokemon>
}