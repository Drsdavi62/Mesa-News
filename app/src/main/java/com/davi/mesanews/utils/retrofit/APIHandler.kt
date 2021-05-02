package com.davi.mesanews.utils.retrofit

import android.app.Application
import androidx.preference.PreferenceManager
import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import com.davi.mesanews.utils.MesaNewsConstants
import retrofit2.Callback

class APIHandler private constructor(val application: Application){



    private val retrofitClient = RetrofitConfig.getRetrofitInstance(MesaNewsConstants.BASE_URL)
    private val endpoint = retrofitClient.create(NewsAPIInterface::class.java)

    val prefs = PreferenceManager.getDefaultSharedPreferences(application)

    companion object {
        @Volatile private var INSTANCE : APIHandler? = null

        fun getInstance(application: Application) : APIHandler {
            if (INSTANCE == null) {
                INSTANCE = APIHandler(application)
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
        val token = prefs.getString(MesaNewsConstants.TOKEN_KEY, "")!!
        val call = endpoint.getNews(token)
        call.enqueue(callback)
    }

    fun getHighlights(callback: Callback<NewsResponseModel>) {
        val token = prefs.getString(MesaNewsConstants.TOKEN_KEY, "")!!
        val call = endpoint.getHighlights(token)
        call.enqueue(callback)
    }
}