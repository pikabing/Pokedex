package com.example.pokemon.api

import android.content.res.Resources.getSystem
import com.example.pokemon.R
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {

    private val BASE_URL = getSystem().getString(R.string.base_url)

    val INSTANCE: PokemonService by lazy {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(PokemonService::class.java)

    }
}