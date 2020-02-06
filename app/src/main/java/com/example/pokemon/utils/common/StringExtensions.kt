package com.example.pokemon.utils.common

import android.content.Context
import android.widget.Toast

fun String.toast(context: Context, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, time).show()
}