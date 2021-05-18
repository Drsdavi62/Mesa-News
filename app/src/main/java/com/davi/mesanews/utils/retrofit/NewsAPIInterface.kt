package com.davi.mesanews.utils.retrofit

import androidx.annotation.RestrictTo
import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import org.koin.androidx.compose.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NewsAPIInterface {
    @GET("/v1/client/news?current_page=2&per_page=&published_at=")
    suspend fun getNews(): NewsResponseModel

    @GET("/v1/client/news/highlights")
    suspend fun getHighlights(): NewsResponseModel

    @POST("v1/client/auth/signin")
    suspend fun performLogin(@Body loginModel: LoginModel): TokenModel
}

class APIService {
    companion object : KoinComponent {
        private val retrofit: Retrofit by inject()
        val service: NewsAPIInterface = retrofit.create(NewsAPIInterface::class.java)
    }
}