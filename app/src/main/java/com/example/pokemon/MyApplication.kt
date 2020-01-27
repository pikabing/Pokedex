package com.example.pokemon

import android.app.Application
import com.example.pokemon.data.db.AppDatabase

class MyApplication : Application() {

    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        AppDatabase.getAppDataBase(this)
    }

}