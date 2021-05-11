package com.davi.mesanews.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.davi.mesanews.models.LoginModel
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.models.TokenModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.NewsErrorTypes
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsObserver: Observer<List<NewsModel>>

    @Mock
    private lateinit var errorsObserver: Observer<List<NewsErrorTypes>>

    @Mock
    private lateinit var highlightsObserver: Observer<List<NewsModel>>

    private lateinit var viewModel: NewsViewModel

    private val news = listOf<NewsModel>(
        NewsModel(
            url = "url",
            "title",
            "descrition",
            "cpntent",
            "author",
            "hoje",
            false,
            "urlImage"
        )
    )
    private val newsResponseModel = NewsResponseModel(news)
    private val resultSuccess = MockAPIRepository(newsResponseModel)

    @Test
    fun `when viewModel getNews get success then sets newsListLiveData`() {
        //Arrange
        viewModel = NewsViewModel(MockFavoritesRepository(), resultSuccess)
        viewModel.newsList.observeForever(newsObserver)

        //Act
        viewModel.getNews()

        //Assert
        verify(newsObserver).onChanged(news)
    }

    @Test
    fun `when viewModel getNews get success and news isFavorite is set`() {
        //Arrange
        viewModel = NewsViewModel(MockFavoritesRepository(news), resultSuccess)
        viewModel.newsList.observeForever(newsObserver)

        //Act
        viewModel.getNews()

        //Assert
        verify(newsObserver).onChanged(news)
        assert(viewModel.newsList.value!![0].isFavorite)
    }

    @Test
    fun `when viewModel getNews fails`() {
        //Arrange
        val resultError = MockAPIRepository(NewsResponseModel(news), true)
        viewModel = NewsViewModel(MockFavoritesRepository(), resultError)
        viewModel.errors.observeForever(errorsObserver)

        //Act
        viewModel.getNews()

        //Assert
        assert(viewModel.errors.value!!.contains(NewsErrorTypes.DateError))
    }

    @Test
    fun `when viewModel getHighlights get success then sets highlightsListLiveData`() {
        //Arrange
        viewModel = NewsViewModel(MockFavoritesRepository(), resultSuccess)
        viewModel.highlightsList.observeForever(highlightsObserver)

        //Act
        viewModel.getHighlights()

        //Assert
        verify(highlightsObserver).onChanged(news)
    }
}

class MockAPIRepository
    (private val newsResponseModel: NewsResponseModel, private val boolean: Boolean = false) :
    NewsAPIInterface {

    override suspend fun getNews(): NewsResponseModel {
        if (boolean) throw RuntimeException()
        return newsResponseModel
    }

    override suspend fun getHighlights(): NewsResponseModel {
        return newsResponseModel
    }

    override suspend fun performLogin(loginModel: LoginModel): TokenModel {
        return TokenModel("")
    }
}

class MockFavoritesRepository(private val newsList: List<NewsModel> = listOf()) :
    FavoritesRepository {
    override suspend fun insertFavorite(newsModel: NewsModel) {
    }

    override suspend fun deleteFavorite(newsModel: NewsModel) {
    }

    override fun getFavoritesList(): LiveData<List<NewsModel>> {
        return MutableLiveData(newsList)
    }
}