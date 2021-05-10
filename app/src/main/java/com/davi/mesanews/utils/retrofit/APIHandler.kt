package com.davi.mesanews.utils.retrofit

import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import retrofit2.Callback
import retrofit2.Retrofit

class APIHandler(
    retrofitClient: Retrofit
) {
    private val endpoint = retrofitClient.create(NewsAPIInterface::class.java)

    fun performLogin(email: String, password: String, callback: Callback<TokenModel>) {
        val loginModel = LoginModel(email, password)

        val call = endpoint.performLogin(loginModel)
        call.enqueue(callback)
    }

    fun getNews(callback: Callback<NewsResponseModel>) {
        val call = endpoint.getNews()
        call.enqueue(callback)
    }

    fun getHighlights(callback: Callback<NewsResponseModel>) {
        val call = endpoint.getHighlights()
        call.enqueue(callback)
    }
}