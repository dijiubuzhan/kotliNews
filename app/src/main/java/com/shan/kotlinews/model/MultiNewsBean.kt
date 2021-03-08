package com.shan.kotlinews.model

/**
 * Author by wuyishan, Date on 2021/3/6.
 */
data class MultiNewsBean(val login_status:Int,val total_number:Int,val has_more:Boolean,val post_content_hint:String,val show_et_status:Int,val feed_flag:Int,
val action_to_last_stick:Int,val message:String,val has_more_to_refresh:Boolean,val tips:TipsBean,val data:List<DataBean>) {
    data class TipsBean(val display_info:String,val open_url:String,val web_url:String,val app_name:String,val package_name:String,val display_template:String,val type:String,val display_duration:Int,val download_url:String)
    data class DataBean(val content:String,val code:String)
}