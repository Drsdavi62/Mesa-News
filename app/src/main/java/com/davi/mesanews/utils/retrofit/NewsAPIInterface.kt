package com.davi.mesanews.utils.retrofit

import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NewsAPIInterface {
    @GET("/v1/client/news?current_page=2&per_page=&published_at=") //fun getNews(@Header("Authorization") authToken : String) : Call<NewsResponseModel>
    fun getNews() : Call<NewsResponseModel>

    @GET("/v1/client/news/highlights")
    fun getHighlights() : Call<NewsResponseModel>

    @POST("v1/client/auth/signin")
    fun performLogin(@Body loginModel : LoginModel) : Call<TokenModel>
}