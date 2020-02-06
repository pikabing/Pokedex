package com.example.pokemon.di.modules

import android.content.Context
import androidx.room.Room
import com.example.pokemon.data.db.AppDatabase
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

}