package com.davi.mesanews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.davi.mesanews.home.HomeActivity
import com.davi.mesanews.news.NewsActivity
import com.davi.mesanews.utils.MesaNewsConstants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val authToken = prefs.getString(MesaNewsConstants.TOKEN_KEY, "")

        var intent : Intent = if (authToken != null && authToken.isNotEmpty()) {
            Intent(this, NewsActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}