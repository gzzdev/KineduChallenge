package com.cuzztomgdev.kineduchallenge.ui.home.adapter

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = false


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val visibleThreshold = layoutManager.spanCount * 3 // Ajuste dinámico

        if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            Log.i("Main Activity", "Loading more comics")
            onLoadMore(totalItemCount)
            loading = true
            //comicsVM.getComics(totalItemCount, 12)
        }
    }

    abstract fun onLoadMore(offset: Int)
}
