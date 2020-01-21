package com.example.pokemon.api

import com.example.pokemon.model.PokemonDetail
import com.example.pokemon.model.PokemonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(
        @Path("id") id: Int
    ): Single<PokemonDetail>
}