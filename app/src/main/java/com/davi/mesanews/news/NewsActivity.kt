package com.davi.mesanews.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.davi.mesanews.R
import com.davi.mesanews.utils.MesaNewsConstants

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_logout_24);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = prefs.edit()
                editor.remove(MesaNewsConstants.TOKEN_KEY)
                editor.apply()
                Navigation.findNavController(findViewById(R.id.news_fragment)).navigate(R.id.logout)
                finish()
            }
        }
        return true
    }
}