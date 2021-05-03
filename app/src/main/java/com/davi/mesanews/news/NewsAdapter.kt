package com.davi.mesanews.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davi.mesanews.R
import com.davi.mesanews.models.NewsModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso

class NewsAdapter(asyncDifferConfig: AsyncDifferConfig<NewsModel>,val onFavoriteClick: OnFavoriteClick) :
    ListAdapter<NewsModel, NewsAdapter.NewsViewHolder>(asyncDifferConfig) {

    interface OnFavoriteClick {
        fun onFavoriteItemClick(news: NewsModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        var news : NewsModel = getItem(position)
        holder.bindView(news)
    }

    class NewsViewHolder(itemView: View, val onFavoriteClick: OnFavoriteClick) : RecyclerView.ViewHolder(itemView) {
        private var title = itemView.findViewById<MaterialTextView>(R.id.news_title)
        private var description = itemView.findViewById<MaterialTextView>(R.id.news_description)
        private var date = itemView.findViewById<MaterialTextView>(R.id.news_date)
        private var image = itemView.findViewById<ImageView>(R.id.news_image)
        private var favoriteButton = itemView.findViewById<ImageButton>(R.id.news_fav_button)

        fun bindView(news : NewsModel) {
            title.text = news.title
            description.text = news.description
            date.text = news.publishedAt

            news.imageUrl.let { strImage ->
                Picasso.get()
                    .load(strImage)
                        //TODO: CHANGE PLACEHOLDER
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(image)
            }

            setButtonText(news.isFavorite)

            favoriteButton.setOnClickListener {
                news.isFavorite = !news.isFavorite
                setButtonText(news.isFavorite)
                onFavoriteClick.onFavoriteItemClick(news)
            }
        }

        private fun setButtonText(isFavorite: Boolean) {
            favoriteButton.setImageResource(if (isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24)
        }
    }
}
