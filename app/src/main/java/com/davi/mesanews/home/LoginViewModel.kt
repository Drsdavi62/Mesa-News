package com.davi.mesanews.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.utils.MesaNewsConstants
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class LoginViewModel(
    retrofitClient: Retrofit,
    prefs: SharedPreferences,
) : ViewModel() {

    private val endpoint = retrofitClient.create(NewsAPIInterface::class.java)

    private val _success = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _success

    private val editor = prefs.edit()

    fun performLogin(email : String, password : String) {
        viewModelScope.launch {
            try {
                val token = endpoint.performLogin(LoginModel(email, password)).token
                editor.putString(MesaNewsConstants.TOKEN_KEY, token)
                editor.apply()
                _success.value = true
            } catch (e: Exception) {
                Log.d("Service Error", e.toString())
                _success.value = false
            }
        }
    }
}