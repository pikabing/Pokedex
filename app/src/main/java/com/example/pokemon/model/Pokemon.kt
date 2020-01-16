package com.example.pokemon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon(var name:String, val url: String, var id: String) : Parcelable