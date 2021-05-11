package com.davi.mesanews.news

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davi.mesanews.R
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.utils.NewsErrorTypes
import com.google.android.material.textview.MaterialTextView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList

class NewsFragment : Fragment(R.layout.news_fragment) {

    private val viewModel: NewsViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var carousel: CarouselView
    private lateinit var spinner: Spinner
    private lateinit var emptyView: LinearLayout
    private var filter = ListFilter.Date
    private var previousErrors = ArrayList<NewsErrorTypes>()

    private lateinit var newsFullList: List<NewsModel>
    private lateinit var favoritesList: List<NewsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.search_item)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint =  getString(R.string.news_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                if (searchText != null) {
                    val activeList = if (filter == ListFilter.Date) newsFullList else favoritesList
                    val carouselVisibility = if (filter == ListFilter.Date) View.VISIBLE else View.GONE

                    if (searchText.isNotEmpty()) {
                        carousel.visibility = View.GONE
                        val tempList = activeList.filter { it.title.contains(searchText, true)}
                        adapter.submitList(tempList as MutableList<NewsModel>)
                    } else {
                        carousel.visibility = carouselVisibility
                        adapter.submitList(activeList as MutableList<NewsModel>)
                    }
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carousel = view.findViewById(R.id.news_carousel)
        emptyView = view.findViewById(R.id.news_empty_view)
        recyclerView = view.findViewById(R.id.news_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.hasFixedSize()

        spinner = view.findViewById(R.id.news_filter_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filters_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    showList(ListFilter.Date)
                } else {
                    showList(ListFilter.Favorite)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        adapter = NewsAdapter(
            AsyncDifferConfig.Builder(NewsModel.DIFF_UTIL).build(),
            object : NewsAdapter.OnFavoriteClick {
                override fun onFavoriteItemClick(news: NewsModel) {
                    viewModel.toggleFavorite(news)
                }
            })

        recyclerView.adapter = adapter

        viewModel.getNews()
        viewModel.getHighlights()

        viewModel.newsList.observe(viewLifecycleOwner, Observer {
            if (filter == ListFilter.Date) {
                if (it.isEmpty()) {
                    changeEmptyState(true)
                } else {
                    newsFullList = it
                    adapter.submitList(it)
                    changeEmptyState(false)
                }
            }
        })

        viewModel.favoritesList.observe(viewLifecycleOwner, Observer {
            favoritesList = it
            if (filter == ListFilter.Favorite) {
                adapter.submitList(it)
            }
        })

        viewModel.highlightsList.observe(viewLifecycleOwner, Observer {
            carousel.apply {
                size = it.size
                resource = R.layout.item_carousel
                autoPlay = true
                indicatorAnimationType = IndicatorAnimationType.THIN_WORM
                carouselOffset = OffsetType.CENTER
                setCarouselViewListener { view, position ->
                    val highlight = it[position]

                    val image = view.findViewById<ImageView>(R.id.carousel_image)
                    highlight.imageUrl.let { strImage ->
                        Picasso.get()
                            .load(strImage)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(image)
                    }

                    val title = view.findViewById<MaterialTextView>(R.id.carousel_title)
                    title.text = highlight.title
                }
                show()
            }
        })

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            handleErrors(it)
        })
    }

    private fun handleErrors(errors: List<NewsErrorTypes>) {
        if (errors.contains(NewsErrorTypes.DateError)) {
            showList(ListFilter.Favorite)
        }

        if (previousErrors.contains(NewsErrorTypes.DateError) && !errors.contains(NewsErrorTypes.DateError)) {
            showList(ListFilter.Date)
        }

        if (errors.contains(NewsErrorTypes.HighlightsError)) {
            carousel.visibility = View.GONE
        } else {
            carousel.visibility = View.VISIBLE
        }

        previousErrors = errors as ArrayList<NewsErrorTypes>
    }

    private fun showList(listFilter: ListFilter) {
        recyclerView.visibility = View.VISIBLE
        if (listFilter == ListFilter.Date) {
            spinner.setSelection(0)
            filter = ListFilter.Date
            viewModel.getNews()
            carousel.visibility = View.VISIBLE
        } else {
            spinner.setSelection(1)
            filter = ListFilter.Favorite
            carousel.visibility = View.GONE
            if (favoritesList.isEmpty()) {
                changeEmptyState(true)
                return
            }
            changeEmptyState(false)
            adapter.submitList(favoritesList)
        }
    }

    private fun changeEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            return
        }
        emptyView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    enum class ListFilter {
        Date, Favorite
    }
}