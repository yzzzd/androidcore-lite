package com.nuvyz.core.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nuvyz.core.BR
import com.nuvyz.core.R
import com.nuvyz.core.databinding.StateLoadingPaginationBinding

open class PaginationLoadState<VB : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : LoadStateAdapter<PaginationLoadState<VB>.NetworkStateItemViewHolder<VB>>() {

    companion object {
        val default = PaginationLoadState<StateLoadingPaginationBinding>(R.layout.state_loading_pagination)
    }

    inner class NetworkStateItemViewHolder<VB : ViewDataBinding>(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            val state = loadState is LoadState.Loading
            binding.setVariable(BR.loading, state)
        }
    }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder<VB>, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): NetworkStateItemViewHolder<VB> {
        val binding = DataBindingUtil.inflate<VB>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return NetworkStateItemViewHolder(binding)
    }

}