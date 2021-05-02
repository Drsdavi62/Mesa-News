package com.davi.mesanews.models

import com.google.gson.annotations.SerializedName

data class NewsResponseModel (
    @SerializedName("data")
    var data : List<NewsModel>
)