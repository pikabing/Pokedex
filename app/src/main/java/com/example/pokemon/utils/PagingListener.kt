package com.example.pokemon.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PagingListener constructor(
    var layoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {

    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems()
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstItemPosition = layoutManager.findFirstVisibleItemPositions(null)[0]

        if (!isLoading() and !isLastPage()) {
            if (visibleItems + firstItemPosition >= totalItems && firstItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }
}