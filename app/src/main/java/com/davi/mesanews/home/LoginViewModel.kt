package com.davi.mesanews.home

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.davi.mesanews.R
import com.davi.mesanews.models.TokenModel
import com.davi.mesanews.utils.retrofit.APIHandler
import com.davi.mesanews.utils.MesaNewsConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val apiHandler: APIHandler,
    private val prefs: SharedPreferences,
    private val navController: NavController
) : ViewModel() {
    var isSuccesfull = MutableLiveData<Boolean>()

    val editor = prefs.edit()

    fun performLogin(email : String, password : String) {
        apiHandler.performLogin(email, password, object : Callback<TokenModel> {
            override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                if (response.body() != null) {
                    editor.putString(MesaNewsConstants.TOKEN_KEY, response.body()!!.token)
                    editor.apply()
                    isSuccesfull.postValue(true)
                    navController.navigate(R.id.action_loginFragment_to_newsActivity)
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