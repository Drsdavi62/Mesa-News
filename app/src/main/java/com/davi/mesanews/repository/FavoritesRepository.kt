package com.davi.mesanews.repository

import androidx.lifecycle.LiveData
import com.davi.mesanews.models.NewsModel

interface FavoritesRepository {
    suspend fun insertFavorite(newsModel: NewsModel)

    suspend fun deleteFavorite(newsModel: NewsModel)

    fun getFavoritesList(): LiveData<List<NewsModel>>
}