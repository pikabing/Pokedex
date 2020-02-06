package com.example.pokemon.di.modules.activity

import com.example.pokemon.contract.DetailContract
import com.example.pokemon.di.scope.ActivityScoped
import com.example.pokemon.presenters.DetailPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class DetailModule {

    @ActivityScoped
    @Binds
    abstract fun bindDetailPresenter(detailPresenter: DetailPresenter): DetailContract.Presenter

}