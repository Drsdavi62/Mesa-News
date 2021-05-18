package com.davi.mesanews.utils.retrofit

import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import retrofit2.Retrofit

class RetrofitDataSource(): NewsAPIInterface {

    override suspend fun getNews(): NewsResponseModel {
        return APIService.service.getNews()
    }

    override suspend fun getHighlights(): NewsResponseModel {
        return APIService.service.getHighlights()
    }

    override suspend fun performLogin(loginModel: LoginModel): TokenModel {
        return APIService.service.performLogin(loginModel)
    }
}