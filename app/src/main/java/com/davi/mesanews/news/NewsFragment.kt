package com.davi.mesanews.news

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davi.mesanews.R
import com.davi.mesanews.models.NewsModel
import com.google.android.material.textview.MaterialTextView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.squareup.picasso.Picasso

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var carousel: CarouselView
    private var filter = ListFilter.Date

    private lateinit var newsFullList: List<NewsModel>
    private lateinit var favoritesList: List<NewsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.search_item)
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
                        val tempList = activeList.filter { it.title.contains(searchText!!, true)}
                        adapter.submitList(tempList)
                    } else {
                        carousel.visibility = carouselVisibility
                        adapter.submitList(activeList)
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
        recyclerView = view.findViewById(R.id.news_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.hasFixedSize()

        val spinner: Spinner = view.findViewById(R.id.news_filter_spinner)
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
                    filter = ListFilter.Date
                    viewModel.getNews()
                    carousel.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    filter = ListFilter.Favorite
                    adapter.submitList(favoritesList)
                    carousel.visibility = View.GONE
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
                newsFullList = it
                adapter.submitList(it)
                recyclerView.visibility = View.VISIBLE
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
    }

    enum class ListFilter {
        Date, Favorite
    }
}