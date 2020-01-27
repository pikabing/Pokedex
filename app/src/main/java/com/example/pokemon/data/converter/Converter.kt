package com.example.pokemon.data.converter

import androidx.room.TypeConverter
import com.example.pokemon.model.Abilities
import com.example.pokemon.model.Types
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

class Converter {

    private val gson = Gson()

    @TypeConverter
    fun typesToJson(types: ArrayList<Types>?): String? {

        return if(types == null) null
        else gson.toJson(types)
    }

    @TypeConverter
    fun jsonToTypes(string: String?): ArrayList<Types>? {
        return if(string == null) null
        else {
            val type = object : TypeToken<ArrayList<Types>>(){}.type
            Gson().fromJson(string, type)
        }
    }

    @TypeConverter
    fun abilitiesToJson(types: ArrayList<Abilities>?): String? {
        return if(types == null) null
        else gson.toJson(types)
    }

    @TypeConverter
    fun jsonToAbilities(string: String?): ArrayList<Abilities>? {
        return if(string == null) null
        else {
            val type = object : TypeToken<ArrayList<Abilities>>(){}.type
            Gson().fromJson(string, type)
        }
    }

    @TypeConverter
    fun jsonArrayToJson(jsonArray: JsonArray?): String? {
        return if(jsonArray == null) null
        else gson.toJson(jsonArray)
    }

    @TypeConverter
    fun jsonToJsonArray(string: String?): JsonArray? {
        return if(string == null) null
        else gson.fromJson(string, JsonArray::class.java)
    }
}