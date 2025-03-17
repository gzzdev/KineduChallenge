package com.cuzztomgdev.kineduchallenge.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cuzztomgdev.kineduchallenge.domain.model.Comic

class ComicDiffCallback(
    private val oldList: List<Comic>,
    private val newList: List<Comic>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}