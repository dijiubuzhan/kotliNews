package com.shan.kotlinews.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shan.kotlinews.R
import com.shan.kotlinews.model.MultiNewsArticleDataBean

/**
 * Author by wuyishan, Date on 2021/3/6.
 */
class RecycleAdapter(val mDatas:List<MultiNewsArticleDataBean>):
    RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        val mTextView:TextView = itemView.findViewById(R.id.txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.setText(mDatas.get(position).title)
    }

    override fun getItemCount() = mDatas.size

}