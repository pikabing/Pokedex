package com.example.pokemon.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.pokemon.data.db.AppDatabase
import com.example.pokemon.data.db.PokemonDao
import com.example.pokemon.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {


    @Provides
    @Singleton
    internal fun provideDatabase(appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext.applicationContext,
        AppDatabase::class.java,
        "myDB"
    ).build()

    @Provides
    @Singleton
    internal fun providePokemonDao(appDatabase: AppDatabase): PokemonDao = appDatabase.pokemonDao()



}