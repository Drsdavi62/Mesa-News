package com.davi.mesanews.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.NewsErrorTypes
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class MockitoNewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

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

    @Spy
    private var newsListLiveData: MutableLiveData<List<NewsModel>> = MutableLiveData()
    @Spy
    private var highlightsLiveData: MutableLiveData<List<NewsModel>> = MutableLiveData()
    @Mock
    private lateinit var newsRepository: NewsAPIInterface

    @Spy
    private var favoritesLiveData: MutableLiveData<List<NewsModel>> = MutableLiveData()
    @Mock
    private lateinit var favoritesRepository: FavoritesRepository

    @Spy
    private var errorsLiveData: MutableLiveData<MutableList<NewsErrorTypes>> = MutableLiveData()


    @Before
    fun setUp() {
        `when`(favoritesRepository.getFavoritesList()).thenReturn(favoritesLiveData)
        viewModel = NewsViewModel(favoritesRepository, newsRepository)
        newsListLiveData = viewModel.newsList as MutableLiveData<List<NewsModel>>
        highlightsLiveData = viewModel.highlightsList as MutableLiveData<List<NewsModel>>
        errorsLiveData = viewModel.errors
    }

    @Test
    fun `when viewModel getNews get success then sets newsListLiveData`(): Unit = runBlocking{
        //Arrange
        `when`(newsRepository.getNews()).thenReturn(newsResponseModel)

        //Act
        viewModel.getNews()

        //Assert
        verify(newsRepository).getNews()
        val newsValue = newsListLiveData.value
        newsValue?.let { assertFalse(it[0].isFavorite) }
        assertEquals(newsValue, news)
        val errors = errorsLiveData.value
        assertNotNull(errors)
        errors?.let { assertFalse(it.contains(NewsErrorTypes.DateError)) }
    }

    @Test
    fun `when viewModel getNews get success and news isFavorite is set`(): Unit = runBlocking {
        //Arrange\
        `when`(newsRepository.getNews()).thenReturn(newsResponseModel)
        favoritesLiveData.value = news

        //Act
        viewModel.getNews()

        //Assert
        verify(newsRepository).getNews()
        val newsValue = newsListLiveData.value
        assertNotNull(newsValue)
        newsValue?.let { assertTrue(it[0].isFavorite) }
        assertEquals(news, newsValue)
        val errors = errorsLiveData.value
        assertNotNull(errors)
        errors?.let { assertFalse(it.contains(NewsErrorTypes.DateError)) }
    }

    @Test
    fun `when viewModel getNews fails`(): Unit = runBlocking {
        //Arrange
        `when`(newsRepository.getNews()).thenAnswer { throw Exception() }

        //Act
        viewModel.getNews()

        //Assert
        val errors = errorsLiveData.value
        assertNotNull(errors)
        errors?.let { assertTrue(it.contains(NewsErrorTypes.DateError)) }
    }

    @Test
    fun `when viewModel getHighlights get success then sets highlightsListLiveData`(): Unit = runBlocking {
        //Arrange
        `when`(newsRepository.getHighlights()).thenReturn(newsResponseModel)

        //Act
        viewModel.getHighlights()

        //Assert
        verify(newsRepository).getHighlights()
        val highlightsValue = highlightsLiveData.value
        assertEquals(highlightsValue, news)
        val errors = errorsLiveData.value
        assertNotNull(errors)
        errors?.let { assertFalse(it.contains(NewsErrorTypes.HighlightsError)) }
    }
}