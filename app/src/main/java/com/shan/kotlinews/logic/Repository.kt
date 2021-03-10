package com.shan.kotlinews.logic


import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.shan.kotlinews.model.MultiNewsArticleDataBean
import com.shan.kotlinews.util.LogUtil
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Author by wuyishan, Date on 2021/3/9.
 */
object Repository {
    val gson = Gson()

    fun searchNewsFromNet() = fire(Dispatchers.IO){
        LogUtil.d("searchNews")
        val dataList: MutableList<MultiNewsArticleDataBean> = java.util.ArrayList()
        val reponse = RxRetrofitUtil.baseUrl("http://toutiao.com/")
            .createApi(ApiService::class.java)
            .getNewsArticle("", (System.currentTimeMillis() / 1000).toString()).await()
        if (reponse.data!=null) {
            for (datum in reponse.data) {
                LogUtil.d( "loadData: datum.content="+datum.content)
                dataList.add(gson.fromJson(datum.content, MultiNewsArticleDataBean::class.java))
            }
        }
        LogUtil.d("dataList="+dataList)
        if (null != dataList && dataList.isNotEmpty()) {
            LogUtil.d("list.size="+dataList.size)
            Result.success(dataList)
        }else{
            Result.failure(RuntimeException("response status is error"))
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                LogUtil.d("fire,Exception e="+e.message)
                Result.failure<T>(e)
            }
            emit(result)
        }

    private suspend fun <T> Call<T>.await() : T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body!=null) {
                        continuation.resume(body)
                    }else{
                        continuation.resumeWithException(java.lang.RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}