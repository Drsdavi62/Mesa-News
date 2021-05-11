package com.davi.mesanews.news

import android.util.Log
import androidx.lifecycle.*
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.utils.NewsErrorTypes
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import com.davi.mesanews.utils.room.dao.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class NewsViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val retrofitClient: Retrofit
) : ViewModel() {

    private val endpoint = retrofitClient.create(NewsAPIInterface::class.java)

    private val _newsList = MutableLiveData<List<NewsModel>>()
    val newsList: LiveData<List<NewsModel>>
        get() = _newsList

    private val _highlights = MutableLiveData<List<NewsModel>>()
    val highlightsList: LiveData<List<NewsModel>>
        get() = _highlights

    val favoritesList: LiveData<List<NewsModel>> = favoritesRepository.getFavoritesList()
    val errors: MutableLiveData<MutableList<NewsErrorTypes>> = MutableLiveData(ArrayList())

    fun getNews() {
        viewModelScope.launch {
            try {
                val news = endpoint.getNews()
                _newsList.value = news.data
            } catch (e: Exception) {
                Log.d("Service error:", e.toString())
            }
        }
    }

    fun getHighlights() {
        viewModelScope.launch {
            try {
                val news = endpoint.getHighlights()
                _highlights.value = news.data
            } catch (e: Exception) {
                Log.d("Service error:", e.toString())
            }
        }
    }

    fun toggleFavorite(newsModel: NewsModel) {
        CoroutineScope(Dispatchers.IO).launch {
            if (newsModel.isFavorite) {
                favoritesRepository.insertFavorite(newsModel)
            } else {
                favoritesRepository.deleteFavorite(newsModel)
            }
        }
    }

    private fun addError(errorType: NewsErrorTypes) {
        val tempErrors = errors.value
        if (errorType !in tempErrors!!) {
            tempErrors.add(errorType)
            errors.postValue(tempErrors)
        }
    }

    private fun removeError(errorType: NewsErrorTypes) {
        val tempErrors = errors.value
        if (errorType in tempErrors!!) {
            tempErrors.remove(errorType)
            errors.postValue(tempErrors)
        }
    }
}