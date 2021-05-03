package com.davi.mesanews.utils.retrofit

import android.content.Context
import androidx.preference.PreferenceManager
import com.davi.mesanews.utils.MesaNewsConstants
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitConfig{
    companion object{
        fun getRetrofitInstance(path: String, context: Context): Retrofit {

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)

            var client = OkHttpClient.Builder().addInterceptor { chain ->
                var request: Request = chain.request()

                val token = prefs.getString(MesaNewsConstants.TOKEN_KEY, "")

                if (token != null && token.isNotEmpty()) {
                    request = request.newBuilder()
                        .addHeader("Authorization", "Bearer $token").build()
                }
                chain.proceed(request)
            }.build()


            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }
}