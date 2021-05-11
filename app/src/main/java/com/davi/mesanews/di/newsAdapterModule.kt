package com.davi.mesanews.di

import androidx.recyclerview.widget.AsyncDifferConfig
import com.davi.mesanews.models.NewsModel
import com.davi.mesanews.news.NewsAdapter
import com.davi.mesanews.utils.MesaNewsConstants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val newsAdapterModule = module {
    scope(named(MesaNewsConstants.NEWS_SCOPE_ID)) {
        scoped { (onFavoriteClick: NewsAdapter.OnFavoriteClick) ->
            provideNewsAdapter(get(), onFavoriteClick)
        }
        factory { AsyncDifferConfig.Builder(NewsModel.DIFF_UTIL).build() }
    }
}

fun provideNewsAdapter(asyncDifferConfig: AsyncDifferConfig<NewsModel>, onFavoriteClick: NewsAdapter.OnFavoriteClick): NewsAdapter {
    return NewsAdapter(
        asyncDifferConfig,
        onFavoriteClick
    )
}