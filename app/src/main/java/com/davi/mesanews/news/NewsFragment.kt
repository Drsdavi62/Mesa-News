package com.davi.mesanews.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davi.mesanews.R
import com.davi.mesanews.models.NewsModel
import com.google.android.material.button.MaterialButton
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carousel = view.findViewById(R.id.news_carousel)
        recyclerView = view.findViewById(R.id.news_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.hasFixedSize()

        val spinner: Spinner = view.findViewById(R.id.news_filter_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filters_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
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
                    // Example here is setting up a full image carousel
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
                // After you finish setting up, show the CarouselView
                show()
            }
        })
    }

    enum class ListFilter {
        Date, Favorite
    }
}