package com.davi.mesanews.utils.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davi.mesanews.models.NewsModel

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(newsModel: NewsModel)

    @Delete
    suspend fun deleteFavorite(newsModel: NewsModel)

    @Query("SELECT * FROM NewsModel")
    fun getFavorites() : LiveData<List<NewsModel>>
}