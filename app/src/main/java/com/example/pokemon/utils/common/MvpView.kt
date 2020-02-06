package com.example.pokemon.utils.common

import android.content.Context

interface MvpView {

    val viewContext : Context?

    fun showErrorToast(string: String) {
        viewContext?.let {
            string.toast(it)
        }
    }
}