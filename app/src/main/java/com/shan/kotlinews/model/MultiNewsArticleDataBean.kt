package com.shan.kotlinews.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author by wuyishan, Date on 2021/3/6.
 */
@Parcelize
data class MultiNewsArticleDataBean(val log_pb:LogPbBean,val read_count:Int,val media_name:String,val ban_comment:Int,val abstractX:String,val ban_bury:Int,val has_video:Boolean,val article_type:Int,val tag:String,val forward_info:ForwardInfoBean,val keywords:String,val rid:String,val label:String,val show_portrait_article:Boolean,val user_verified:Int,val aggr_type:Int,val cell_type:Int,val article_sub_type:Int,val bury_count:Int,val title:String,val ignore_web_transform:Int,val source_icon_style:Int,val tip:Int,val hot:Int,val share_url:String,
val has_mp4_video:Int,val source:String,val comment_count:Int,val article_url:String,val share_count:Int,val stick_label:String,val publish_time:Int,val has_image:Boolean,val cell_layout_style:Int,val tag_id:Long,val video_style:Int,val verified_content:String,val display_url:String,val item_id:Long,val is_subject:Boolean,val stick_style:Int,val show_portrait:Boolean,val repin_count:Int,val cell_flag:Int,val user_info:UserInfoBean,val source_open_url:String,val level:Int,val digg_count:Int,val behot_time:String,val article_alt_url:String,val cursor:Long,val url:String,val preload_web:Int,val user_repin:Int,val label_style:Int,val item_version:Int,val media_info:MediaInfoBean,val group_id:Long,val middle_image:MiddleImageBean,val gallary_image_count:Int) :
    Parcelable {
    @Parcelize
    data class LogPbBean(val impr_id:String): Parcelable
    @Parcelize
    data class ForwardInfoBean(val forward_count:Int) : Parcelable
    @Parcelize
    data class UserInfoBean(val verified_content:String,val avatar_url:String,val user_id:Long,val name:String,val follower_count:Int,val follow:Boolean,val user_auth_info:String,val user_verified:Boolean,val description:String) :
        Parcelable

    @Parcelize
    data class MediaInfoBean(val user_id:Long,val verified_content:String,val avatar_url:String,val media_id:String,val name:String,val recommend_type:Int,val follow:Boolean,val recommend_reason:String,val is_star_user:Boolean,val user_verified:Boolean) :
        Parcelable
    @Parcelize
    data class MiddleImageBean(val url:String,val width:Int,val uri:String,val height:Int,val url_list:List<UrlListBean>): Parcelable
    @Parcelize
    data class UrlListBean(val url:String): Parcelable
    data class VideoDetailInfoBean(val group_flags:Int,val video_type:Int,val video_preloading_flag:Int,val direct_play:Int,val detail_video_large_image:DetailVideoLargeImageBean,val show_pgc_subscribe:Int,val video_third_monitor_url:String,val video_id:String,val video_watching_count:Int,val video_watch_count:Int,val video_url:List<*>)
    data class DetailVideoLargeImageBean(val url:String,val width:Int,val uri:String,val height:Int,val url_list:List<MiddleImageBean>)
    data class LargeImageListBean(val urlX:String,val width:Int,val uri:String,val height:Int,val url_list:List<MiddleImageBean>)
    data class ImageListBean(val url:String,val width:Int,val uri:String,val height:Int,val url_list:List<LargeImageListBean>)
}