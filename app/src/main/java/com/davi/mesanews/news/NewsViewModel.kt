package com.davi.mesanews.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.utils.NewsErrorTypes
import com.davi.mesanews.utils.retrofit.APIHandler
import com.davi.mesanews.utils.room.dao.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    var favoritesRepository = FavoritesRepository(application)
    var newsList : MutableLiveData<List<NewsModel>> = MutableLiveData()
    var highlightsList : MutableLiveData<List<NewsModel>> = MutableLiveData()
    val favoritesList: LiveData<List<NewsModel>> = favoritesRepository.getFavoritesList()
    val errors:  MutableLiveData<MutableList<NewsErrorTypes>> = MutableLiveData(ArrayList())

    private val apiHandler = APIHandler.getInstance(application.applicationContext)

    fun getNews() {
        apiHandler.getNews(object : Callback<NewsResponseModel> {
            override fun onFailure(call: Call<NewsResponseModel>, t: Throwable) {
                addError(NewsErrorTypes.DateError)
            }

            override fun onResponse(
                call: Call<NewsResponseModel>,
                response: Response<NewsResponseModel>
            ) {
                if (response.body() == null || response.code() >= 400) {
                    addError(NewsErrorTypes.DateError)
                    return
                }
                removeError(NewsErrorTypes.DateError)
                var news = response.body()!!.data
                news = news.sortedBy { it.publishedAt }
                news.map { newsModel ->
                    newsModel.isFavorite =
                        favoritesList.value!!.any { it.url == newsModel.url }
                }
                newsList.postValue(news)
            }
        })
    }

    fun getHighlights() {
        apiHandler.getHighlights(object : Callback<NewsResponseModel> {
            override fun onFailure(call: Call<NewsResponseModel>, t: Throwable) {
                addError(NewsErrorTypes.HighlightsError)
            }

            override fun onResponse(
                call: Call<NewsResponseModel>,
                response: Response<NewsResponseModel>
            ) {
                if (response.body() == null || response.code() >= 400) {
                    addError(NewsErrorTypes.HighlightsError)
                    return
                }
                removeError(NewsErrorTypes.HighlightsError)
                var news = response.body()!!.data
                news = news.sortedBy { it.publishedAt }
                highlightsList.postValue(news)
            }
        })
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