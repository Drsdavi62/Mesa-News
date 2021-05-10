package com.davi.mesanews.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.davi.mesanews.utils.MesaNewsConstants
import com.davi.mesanews.utils.retrofit.APIHandler
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideOkHttpClient(androidContext()) }
    single { provideRetrofit(get()) }
    single {
        APIHandler(get())
    }
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(MesaNewsConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun provideOkHttpClient(context: Context): OkHttpClient {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    return OkHttpClient.Builder().addInterceptor { chain ->
        var request: Request = chain.request()

        val token = prefs.getString(MesaNewsConstants.TOKEN_KEY, "")

        if (token != null && token.isNotEmpty()) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token").build()
        }
        chain.proceed(request)
    }.build()
}
