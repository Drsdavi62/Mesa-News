package com.davi.mesanews.news

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import androidx.lifecycle.Observer
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
import java.nio.file.DirectoryStream
import java.util.*

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var carousel : CarouselView
    private var filter = ListFilter.Date

    private lateinit var favoritesList : List<NewsModel>

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

        adapter = NewsAdapter(AsyncDifferConfig.Builder(NewsModel.DIFF_UTIL).build(), object : NewsAdapter.OnFavoriteClick {
            override fun onFavoriteItemClick(news: NewsModel) {
                viewModel.toggleFavorite(news)
            }
        })

        recyclerView.adapter = adapter

        viewModel.getNews()
        viewModel.getHighlights()

        view.findViewById<MaterialButton>(R.id.news_see_fav).setOnClickListener {
            filter = ListFilter.Favorite
            adapter.submitList(favoritesList)
            carousel.visibility = View.GONE
        }

        view.findViewById<MaterialButton>(R.id.news_see_all).setOnClickListener {
            filter = ListFilter.Date
            viewModel.getNews()
            carousel.visibility = View.VISIBLE
        }

        viewModel.newsList.observe(viewLifecycleOwner, Observer {
            if (filter == ListFilter.Date) {
                adapter.submitList(it)
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
                            //TODO: CHANGE PLACEHOLDER
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