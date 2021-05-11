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
    suspend fun getNews() : NewsResponseModel

    @GET("/v1/client/news/highlights")
    suspend fun getHighlights() : NewsResponseModel

    @POST("v1/client/auth/signin")
    suspend fun performLogin(@Body loginModel : LoginModel) : TokenModel
}