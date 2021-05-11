package com.davi.mesanews.news

import android.util.Log
import androidx.lifecycle.*
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.NewsErrorTypes
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import com.davi.mesanews.utils.room.dao.DatabaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val newsAPIInterface: NewsAPIInterface
) : ViewModel() {

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
                var news = newsAPIInterface.getNews().data
                news = news.sortedBy { it.publishedAt }
                news.map { newsModel ->
                    newsModel.isFavorite =
                        favoritesList.value != null &&
                        favoritesList.value!!.any { it.url == newsModel.url }
                }
                _newsList.value = news
                removeError(NewsErrorTypes.DateError)
            } catch (e: Exception) {
                Log.d("Service error:", e.toString())
                addError(NewsErrorTypes.DateError)
            }
        }
    }

    fun getHighlights() {
        viewModelScope.launch {
            try {
                val news = newsAPIInterface.getHighlights()
                _highlights.value = news.data
            } catch (e: Exception) {
                Log.d("Service error:", e.toString())
                addError(NewsErrorTypes.HighlightsError)
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