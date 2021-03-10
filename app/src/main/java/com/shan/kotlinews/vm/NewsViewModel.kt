package com.shan.kotlinews.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shan.kotlinews.logic.Repository
import com.shan.kotlinews.model.MultiNewsArticleDataBean
import com.shan.kotlinews.util.LogUtil

/**
 * Author by wuyishan, Date on 2021/3/8.
 */
class NewsViewModel:ViewModel() {
    val netTime = MutableLiveData<Long>()
    val oldItems = ArrayList<MultiNewsArticleDataBean>()
    val newItems = ArrayList<MultiNewsArticleDataBean>()

    val netItems = Transformations.switchMap(netTime){
        Repository.searchNewsFromNet()
    }

    fun searchNews(){
        LogUtil.d("searchNews")
        netTime.value = System.currentTimeMillis()
    }
}