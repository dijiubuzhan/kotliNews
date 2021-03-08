package com.shan.kotlinews.model

/**
 * Author by wuyishan, Date on 2021/3/6.
 */
data class NewsBean(val data:Long,val stories:List<Story>) {
    data class Story(val images:List<String>,val type:Int,val id:Long,val ga_prefix:Long,val title:String)
}