package com.example.pokemon.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagingListener(var layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems()
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()

        if(!isLoading() and !isLastPage()) {
            if (visibleItems + firstItemPosition >= totalItems && firstItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }
}