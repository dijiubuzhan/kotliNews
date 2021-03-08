package com.shan.kotlinews.ui

import androidx.recyclerview.widget.DiffUtil
import com.shan.kotlinews.model.MultiNewsArticleDataBean

class DiffCallback private constructor(
    oldItems: List<MultiNewsArticleDataBean>,
    mNewItems: List<MultiNewsArticleDataBean>
) : DiffUtil.Callback() {
    private val mOldItems: List<MultiNewsArticleDataBean>?
    private val mNewItems: List<MultiNewsArticleDataBean>?
    override fun getOldListSize(): Int {
        return mOldItems?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return mNewItems?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldItems!![oldItemPosition].equals(mNewItems!![newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldItems!![oldItemPosition].hashCode() === mNewItems!![newItemPosition].hashCode()
    }

    companion object {
        fun create(
            oldList: List<MultiNewsArticleDataBean>,
            newList: List<MultiNewsArticleDataBean>,
            adapter: RecycleAdapter
        ) {
            val diffCallback = DiffCallback(oldList, newList)
            val result = DiffUtil.calculateDiff(diffCallback, true)
            result.dispatchUpdatesTo(adapter)
        }
    }

    init {
        mOldItems = oldItems
        this.mNewItems = mNewItems
    }
}