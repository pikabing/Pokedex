package com.example.pokemon.api

import com.example.pokemon.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {

    private const val URL = BuildConfig.BASE_URL

    val INSTANCE: PokemonService by lazy {

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(PokemonService::class.java)

    }
}