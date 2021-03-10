package com.shan.kotlinews.logic

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author by wuyishan, Date on 2021/3/6.
 */
object RxRetrofitUtil {
    val mRetrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())


    fun baseUrl(baseUrl:String):RxRetrofitUtil{
        mRetrofitBuilder.baseUrl(baseUrl)
        return this
    }

    fun <T> createApi(cls:Class<T>):T{
        return mRetrofitBuilder.build().create(cls)
    }


}