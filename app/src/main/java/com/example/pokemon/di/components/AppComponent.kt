package com.example.pokemon.di.components

import android.app.Application
import com.example.pokemon.MyApplication
import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, ActivityModule::class,
        ActivityBindingModule::class, ApiModule::class, DbModule::class, AppModule::class]
)
interface AppComponent : AndroidInjector<MyApplication> {

    fun providePokemonRepository(): PokemonRepository
    fun provideRetrofit(): Retrofit

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(pokemonAdapter: PokemonAdapter) : PokemonAdapter

}