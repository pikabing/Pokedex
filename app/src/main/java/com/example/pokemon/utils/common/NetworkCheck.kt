package com.example.pokemon.utils.common

import android.content.Context
import android.net.ConnectivityManager


object NetworkCheck {

    fun isConnectedToNetwork(context: Context?): Boolean {
        if (context == null)
            return false

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val info = connectivityManager?.activeNetworkInfo
        return info != null && info.isConnected
    }
}
