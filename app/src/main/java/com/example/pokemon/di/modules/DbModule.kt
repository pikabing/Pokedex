package com.example.pokemon.di.modules

import android.app.Application
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
    internal fun provideDatabase(application: Application): AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "myDB"
    ).build()

    @Provides
    @Singleton
    internal fun providePokemonDao(appDatabase: AppDatabase): PokemonDao = appDatabase.pokemonDao()



}