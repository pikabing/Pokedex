package com.example.pokemon.di.modules

import com.example.pokemon.di.scope.ActivityScoped
import com.example.pokemon.ui.DetailActivity
import com.example.pokemon.ui.FavoritesActivity
import com.example.pokemon.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    internal abstract fun bindMainActivity(): MainActivity



    @ActivityScoped
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    internal abstract fun bindDetailActivity(): DetailActivity



    @ActivityScoped
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    internal abstract fun bindFavoriteActivity(): FavoritesActivity

}