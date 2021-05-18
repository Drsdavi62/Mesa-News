package com.davi.mesanews

//import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.news.NewsActivity
import com.davi.mesanews.news.NewsViewModel
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.android.*
import org.robolectric.Robolectric;


@RunWith(AndroidJUnit4ClassRunner::class)
class NewsFragmentTest {

    private lateinit var activity: NewsActivity

    private lateinit var activityController: ActivityController<NewsActivity>

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var viewModel: NewsViewModel

    @Mock
    private lateinit var newsLiveData: MutableLiveData<List<NewsModel>>

    @Before
    fun setUp() {
        `when`(viewModel.newsList).thenReturn(newsLiveData)
        activityController = Robolectric.buildActivity(NewsActivity::class.java)
        activity = activityController.get()

        activityController.create()
        //activity.setTes
    }
}