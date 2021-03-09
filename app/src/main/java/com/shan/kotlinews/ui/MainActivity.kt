package com.shan.kotlinews.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.shan.kotlinews.R
import com.shan.kotlinews.model.MultiNewsArticleDataBean
import com.shan.kotlinews.util.LogUtil
import com.shan.kotlinews.vm.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivityxx"
    }

    lateinit var m_RecycleAdapter: RecycleAdapter
    var lastItemCount = 0
    val viewModel by lazy { ViewModelProviders.of(this).get(NewsViewModel::class.java) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d( "onCreate: ")
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d("onDestroy: ")
    }

    private fun doSetAdapter(list: List<MultiNewsArticleDataBean>) {
        if (viewModel.newItems.size > 150) {
            viewModel.newItems.clear()
        }
        viewModel.newItems.addAll(list)
        DiffCallback.create(viewModel.oldItems, viewModel.newItems, m_RecycleAdapter!!)
        viewModel.oldItems.clear()
        viewModel.oldItems.addAll(viewModel.newItems)
        LogUtil.d("doSetAdapter,viewModel.oldItems.size="+viewModel.oldItems.size+",,viewModel.oldItems="+viewModel.oldItems)
        swipeRefresh!!.stopNestedScroll()
    }

    private fun initView() {
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        swipeRefresh.setOnRefreshListener {
            viewModel.searchNews()
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
        m_RecycleAdapter = RecycleAdapter(viewModel.oldItems)
        if (viewModel.oldItems !=null) {
            LogUtil.d("viewModel.oldItems.size="+viewModel.oldItems.size+",,viewModel.oldItems="+viewModel.oldItems)
        }else{
            LogUtil.d("viewModel.oldItems ==null")
        }
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
                viewModel.searchNews()
            }
        }
        viewModel.netItems.observe(this, { items ->
            swipeRefresh!!.isRefreshing = false
            if (items !=null) {
                doSetAdapter(items)
            }else{
                Toast.makeText(this@MainActivity, "update error", Toast.LENGTH_SHORT).show()
            }

        })

    }
}