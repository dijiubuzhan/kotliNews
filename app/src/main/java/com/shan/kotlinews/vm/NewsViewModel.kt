package com.shan.kotlinews.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.shan.kotlinews.logic.ApiService
import com.shan.kotlinews.logic.RxRetrofitUtil
import com.shan.kotlinews.model.MultiNewsArticleDataBean
import com.shan.kotlinews.model.MultiNewsBean
import com.shan.kotlinews.ui.MainActivity
import com.shan.kotlinews.util.LogUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Function

/**
 * Author by wuyishan, Date on 2021/3/8.
 */
class NewsViewModel:ViewModel() {
    val netItems = MutableLiveData<List<MultiNewsArticleDataBean>>()
    val oldItems = ArrayList<MultiNewsArticleDataBean>()
    val newItems = ArrayList<MultiNewsArticleDataBean>()
    val gson = Gson()

    fun searchNews(){
        LogUtil.d("searchNews")
        RxRetrofitUtil.baseUrl("http://toutiao.com/")
            .createApi(ApiService::class.java)
            .getNewsArticle("", (System.currentTimeMillis() / 1000).toString())
            .subscribeOn(Schedulers.newThread())
            .switchMap(Function { multiNewsModel: MultiNewsBean ->
                val dataList: MutableList<MultiNewsArticleDataBean> = java.util.ArrayList()
                for (datum in multiNewsModel.data) {
                    Log.d(MainActivity.TAG, "loadData: datum.content="+datum.content)
                    dataList.add(gson.fromJson(datum.content, MultiNewsArticleDataBean::class.java))
                }
                Observable.fromIterable(dataList)
            } as Function<MultiNewsBean, Observable<MultiNewsArticleDataBean>>)
            .toList()
            .map { list: List<MultiNewsArticleDataBean> ->
                // 过滤重复新闻(与本次刷新的数据比较,因为使用了2个请求,数据会有重复)
                var i: Int = 0;
                var j: Int = list.size - 1;
                for (i in 0 until list.size - 1) {
                    for (j in list.size - 1 downTo i + 1) {
                        if (list.get(j).title.equals(list.get(i).title)) {

                            //list.remove(j);
                        }
                    }
                }
                list;
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<MultiNewsArticleDataBean>? ->
                if (null != list && list.size > 0) {
                    netItems.value = list
                } else {
                    netItems.value = null
                }
            }) { throwable: Throwable ->
                netItems.value = null
            }
    }
}