package com.davi.mesanews.utils.retrofit

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import com.davi.mesanews.utils.MesaNewsConstants
import retrofit2.Callback
import retrofit2.Retrofit

class APIHandler(context: Context){

    private val retrofitClient = RetrofitConfig.getRetrofitInstance(MesaNewsConstants.BASE_URL, context)
    private val endpoint = retrofitClient.create(NewsAPIInterface::class.java)

    companion object {
        @Volatile private var INSTANCE : APIHandler? = null

        fun getInstance(context: Context) : APIHandler {
            if (INSTANCE == null) {
                INSTANCE = APIHandler(context)
            }
            return INSTANCE!!
        }
    }

    fun performLogin(email: String, password:  String, callback: Callback<TokenModel>) {
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