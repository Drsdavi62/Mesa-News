package com.davi.mesanews.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class NewsModel (
    @PrimaryKey
    @SerializedName("url")
    var url : String,

    @SerializedName("title")
    var title : String,

    @SerializedName("description")
    var description : String,

    @SerializedName("content")
    var content : String,

    @SerializedName("author")
    var author : String,

    @SerializedName("published_at")
    var publishedAt : String,

    @SerializedName("highlight")
    var isHighlight : Boolean,

    @SerializedName("image_url")
    var imageUrl : String,

    var isFavorite: Boolean = false
) {

    companion object {
        val DIFF_UTIL = object: DiffUtil.ItemCallback<NewsModel>() {
            override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel) = oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.publishedAt == newItem.publishedAt &&
                        oldItem.imageUrl == newItem.imageUrl &&
                        oldItem.isFavorite == newItem.isFavorite
            }

        }
    }
}