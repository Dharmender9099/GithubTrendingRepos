package com.example.trendingrepos.ui.trendingrepos.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.trendingrepos.data.TrendingRepo

class TrendingReposDiffUtilCallback(
    private val oldList: List<TrendingRepo>,
    private val newList: List<TrendingRepo>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result = newList[newItemPosition].compareTo(oldList[oldItemPosition])
        return result == 1
    }
}