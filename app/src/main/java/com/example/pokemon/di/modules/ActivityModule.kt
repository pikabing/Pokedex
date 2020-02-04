package com.example.pokemon.di.modules

import com.example.pokemon.adapter.PokemonAdapter
import com.example.pokemon.contract.MainContract
import com.example.pokemon.contract.DetailContract
import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.di.scope.ActivityScoped
import com.example.pokemon.presenters.MainPresenter
import com.example.pokemon.presenters.DetailPresenter
import com.example.pokemon.presenters.FavoritePresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ActivityModule {

    @ActivityScoped
    @Binds
    abstract fun bindMainPresenter(presenter: MainPresenter): MainContract.Presenter


    @ActivityScoped
    @Binds
    abstract fun bindDetailPresenter(detailPresenter: DetailPresenter): DetailContract.Presenter


    @ActivityScoped
    @Binds
    abstract fun bindHomePresenter(presenter: FavoritePresenter): FavoritesContract.Presenter

}