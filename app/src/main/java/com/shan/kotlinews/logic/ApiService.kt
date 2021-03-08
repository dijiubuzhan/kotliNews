package com.shan.kotlinews.logic

/**
 * Author by wuyishan, Date on 2021/3/6.
 */

import com.shan.kotlinews.model.MultiNewsBean
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    /*@POST("forecast")
    @FormUrlEncoded
    fun postRequest(@Field("city") city:String,@Field("key") key:String): Observable<WeatherBean>*/

    @GET("http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
    fun getNewsArticle(@Query("category") category:String,
                       @Query("max_behot_time") maxBehotTime:String):Observable<MultiNewsBean>
}