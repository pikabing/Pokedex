package com.example.pokemon.di.modules.activity

import com.example.pokemon.contract.MainContract
import com.example.pokemon.di.scope.ActivityScoped
import com.example.pokemon.presenters.MainPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class MainModule {

    @ActivityScoped
    @Binds
    abstract fun bindMainPresenter(presenter: MainPresenter): MainContract.Presenter

}