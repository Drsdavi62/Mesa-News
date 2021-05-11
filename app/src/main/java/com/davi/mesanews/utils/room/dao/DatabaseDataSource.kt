package com.davi.mesanews.utils.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.room.FavoritesDatabase

class DatabaseDataSource (private val context: Context): FavoritesRepository {
    private val favoritesDao: FavoritesDao
    
    init {
        val database = FavoritesDatabase.getDatabase(context)
        favoritesDao = database.favoritesDao()
    }
    
    override suspend fun insertFavorite(newsModel: NewsModel) {
        newsModel.isFavorite = true
        favoritesDao.insertFavorite(newsModel)
    }

    override suspend fun deleteFavorite(newsModel: NewsModel) {
        favoritesDao.deleteFavorite(newsModel)
    }

    override fun getFavoritesList(): LiveData<List<NewsModel>> = favoritesDao.getFavorites()
}