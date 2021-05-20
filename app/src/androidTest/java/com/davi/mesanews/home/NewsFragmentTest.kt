package com.davi.mesanews.home

import android.content.Intent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.davi.mesanews.R
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.models.NewsResponseModel
import com.davi.mesanews.news.NewsActivity
import com.davi.mesanews.news.NewsFragment
import com.davi.mesanews.news.NewsViewModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.retrofit.NewsAPIInterface
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class NewsFragmentTest {

    @get:Rule
    val rule = ActivityTestRule(NewsActivity::class.java, true, false)

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var newsRepository: NewsAPIInterface

    @Spy
    private var favoritesLiveData: MutableLiveData<List<NewsModel>> = MutableLiveData()
    @Mock
    private lateinit var favoritesRepository: FavoritesRepository

    private lateinit var mockModule: Module

    private val news = listOf<NewsModel>(
        NewsModel(
            url = "url",
            "title",
            "description",
            "content",
            "author",
            "today",
            false,
            "urlImage"
        )
    )
    private val newsResponseModel = NewsResponseModel(news)

    @Before
    fun setUp(): Unit = runBlocking {
        `when`(favoritesRepository.getFavoritesList()).thenReturn(favoritesLiveData)
        `when`(newsRepository.getNews()).thenReturn(newsResponseModel)
        mockModule = module {
            viewModel (override = true) {
                NewsViewModel(favoritesRepository, newsRepository)
            }
        }
        loadKoinModules(mockModule)
    }

    @After
    fun tearDown() {
        unloadKoinModules(mockModule)
    }

    @Test
    fun test_isRecyclerViewVisible(): Unit = runBlocking {
        rule.launchActivity(Intent())

        onView(withId(R.id.news_recycler)).check(matches(isDisplayed()))
        onView(withText(news[0].title)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isCarouselInvisibleOnError(): Unit = runBlocking {
        `when`(newsRepository.getHighlights()).thenAnswer { throw RuntimeException() }
        rule.launchActivity(Intent())

        onView(withId(R.id.news_carousel)).check(matches(not(isDisplayed())))
    }
}
