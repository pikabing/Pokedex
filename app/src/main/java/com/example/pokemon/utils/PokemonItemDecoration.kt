package com.example.pokemon.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class PokemonItemDecoration(var spacing: Int) : RecyclerView.ItemDecoration() {

    val gridSize = 2
    private var mNeedLeftSpacing = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val frameWidth = ((parent.getWidth() - spacing * (gridSize - 1)) / gridSize)
        val padding: Int = parent.getWidth() / gridSize - frameWidth
        val itemPosition =
            (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < gridSize) {
            outRect.top = spacing
        } else {
            outRect.top = spacing
        }
        if (itemPosition % gridSize == 0) {
            outRect.left = spacing
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % gridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = spacing
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = spacing - padding
            if ((itemPosition + 2) % gridSize == 0) {
                outRect.right = spacing - padding
            } else {
                outRect.right = spacing / 2
            }
        } else if ((itemPosition + 2) % gridSize == 0) {
            mNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
        outRect.bottom = 0
    }
}