package com.nuvyz.core.base.adapter

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

open class BasePagingSource<T : Any> constructor(private val firstPageIndex: Int, private val block: suspend (page: Int, limit: Int) -> List<T>): PagingSource<Int, T>(){

    companion object {
        fun config(itemPerPage: Int = 10) = PagingConfig(pageSize = itemPerPage, initialLoadSize = itemPerPage)
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: firstPageIndex
            val data = block(page, params.loadSize)
            val nextPage = if (data.size < params.loadSize) {
                null
            } else {
                page + 1
            }
            val prevPage = if (page == firstPageIndex) null else page - 1
            return LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}