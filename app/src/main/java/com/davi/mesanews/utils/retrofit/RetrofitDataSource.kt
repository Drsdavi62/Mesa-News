package com.davi.mesanews.utils.retrofit

import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import retrofit2.Retrofit

class RetrofitDataSource(retrofit: Retrofit): NewsAPIInterface {

    private val endpoint = retrofit.create(NewsAPIInterface::class.java)

    override suspend fun getNews(): NewsResponseModel {
        return endpoint.getNews()
    }

    override suspend fun getHighlights(): NewsResponseModel {
        return endpoint.getHighlights()
    }

    override suspend fun performLogin(loginModel: LoginModel): TokenModel {
        return endpoint.performLogin(loginModel)
    }
}