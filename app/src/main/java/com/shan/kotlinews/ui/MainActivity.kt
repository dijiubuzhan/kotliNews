package com.shan.kotlinews.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.shan.kotlinews.R
import com.shan.kotlinews.logic.ApiService
import com.shan.kotlinews.logic.RxRetrofitUtil
import com.shan.kotlinews.model.MultiNewsArticleDataBean
import com.shan.kotlinews.model.MultiNewsBean
import io.reactivex.functions.Function

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivityxx"
    }
    var oldItems: MutableList<MultiNewsArticleDataBean> = ArrayList()
    var dataModelList: MutableList<MultiNewsArticleDataBean> = ArrayList()
    lateinit var m_RecycleAdapter: RecycleAdapter
    var lastItemCount = 0
    val gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        loadData()

    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        /* CoroutineScope(Dispatchers.Main).launch{
             val task1 = withContext(Dispatchers.IO){
                   RxRetrofitUtil.baseUrl("http://toutiao.com/")
                     .createApi(ApiService::class.java)
                     .getNewsArticle("", (System.currentTimeMillis() / 1000).toString())
             }
             val dataList: List<MultiNewsArticleDataBean> = java.util.ArrayList<MultiNewsArticleDataBean>()
             task1.data 。for
         }*/

        RxRetrofitUtil.baseUrl("http://toutiao.com/")
            .createApi(ApiService::class.java)
            .getNewsArticle("", (System.currentTimeMillis() / 1000).toString())
            .subscribeOn(Schedulers.newThread())
            .switchMap(Function { multiNewsModel: MultiNewsBean ->
                val dataList: MutableList<MultiNewsArticleDataBean> = ArrayList()
                for (datum in multiNewsModel.data) {
                    Log.d(TAG, "loadData: datum.content="+datum.content)
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
                    doSetAdapter(list)
                } else {
                    Toast.makeText(this@MainActivity, "no more data", Toast.LENGTH_SHORT).show()
                }
                swipeRefresh!!.isRefreshing = false
            }) { throwable: Throwable ->
                Toast.makeText(this@MainActivity, "update error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onError: Throwable e=" + throwable.message)
                swipeRefresh!!.isRefreshing = false
            }
    }

    private fun doSetAdapter(list: List<MultiNewsArticleDataBean>) {
        if (dataModelList.size > 150) {
            dataModelList.clear()
        }
        dataModelList.addAll(list)
        DiffCallback.create(oldItems, dataModelList, m_RecycleAdapter!!)
        oldItems.clear()
        oldItems.addAll(dataModelList)
        swipeRefresh!!.stopNestedScroll()
    }

    private fun initView() {
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        swipeRefresh.setOnRefreshListener {
            loadData()
        }
        nav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.hello -> {
                    startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
                }
                else -> Toast.makeText(this@MainActivity, "I am green", Toast.LENGTH_SHORT).show()
            }
            it.isChecked = true
            drawer.closeDrawers()
            true
        })
        m_RecycleAdapter = RecycleAdapter(oldItems)
        recycle_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle_view.setHasFixedSize(true)
        recycle_view.itemAnimator = DefaultItemAnimator()
        recycle_view.adapter = m_RecycleAdapter
        recycle_view.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val layoutManager = recycle_view.layoutManager as LinearLayoutManager
            val itemCount = layoutManager!!.itemCount
            val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
                lastItemCount = itemCount
                loadData()
            }
        }

    }
}