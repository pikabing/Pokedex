package com.example.pokemon.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.pokemon.model.Pokemon

class DiffUtilCallBack(private val oldList: List<Pokemon>, private val newList: List<Pokemon>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].favorite == newList[newItemPosition].favorite

}