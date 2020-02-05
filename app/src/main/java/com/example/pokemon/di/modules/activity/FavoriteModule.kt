package com.example.pokemon.di.modules.activity

import com.example.pokemon.contract.FavoritesContract
import com.example.pokemon.di.scope.ActivityScoped
import com.example.pokemon.presenters.FavoritePresenter
import dagger.Binds
import dagger.Module

@Module
abstract class FavoriteModule {

    @ActivityScoped
    @Binds
    abstract fun bindFavoritePresenter(presenter: FavoritePresenter): FavoritesContract.Presenter
}