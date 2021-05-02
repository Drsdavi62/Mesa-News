package com.davi.mesanews.home

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.davi.mesanews.models.TokenModel
import com.davi.mesanews.utils.retrofit.APIHandler
import com.davi.mesanews.utils.MesaNewsConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var isSuccesfull = MutableLiveData<Boolean>()
    val apiHandler = APIHandler.getInstance(application)

    val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    val editor = prefs.edit()

    fun performLogin(email : String, password : String) {
        apiHandler.performLogin(email, password, object : Callback<TokenModel> {
            override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                if (response.body() != null) {
                    editor.putString(MesaNewsConstants.TOKEN_KEY, response.body()!!.token)
                    editor.apply()
                    isSuccesfull.postValue(true)
                } else {
                    isSuccesfull.postValue(false)
                }

            }

            override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                editor.remove(MesaNewsConstants.TOKEN_KEY)
                editor.apply()
                isSuccesfull.postValue(false)
            }

        })
    }
}