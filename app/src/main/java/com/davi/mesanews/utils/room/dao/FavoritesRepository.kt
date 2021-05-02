package com.davi.mesanews.utils.room.dao

import android.content.Context
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.utils.room.FavoritesDatabase

class FavoritesRepository (private val context: Context) {
    private val favoritesDao: FavoritesDao
    
    init {
        val database = FavoritesDatabase.getDatabase(context)
        favoritesDao = database.favoritesDao()
    }
    
    suspend fun insertFavorite(newsModel: NewsModel) {
        newsModel.isFavorite = true
        favoritesDao.insertFavorite(newsModel)
    }

    suspend fun deleteFavorite(newsModel: NewsModel) {
        favoritesDao.deleteFavorite(newsModel)
    }
    
    fun getFavoritesList() = favoritesDao.getFavorites()
}