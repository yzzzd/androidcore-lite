package com.nuvyz.core.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nuvyz.core.BR
import com.nuvyz.core.R
import com.nuvyz.core.databinding.ItemPlaceholderBinding
import com.nuvyz.core.databinding.ItemPlaceholderGridBinding

open class BaseListAdapter<VB: ViewDataBinding, T: Any>(@LayoutRes private val layoutRes: Int) : ListAdapter<T, BaseListAdapter<VB, T>.ItemViewHolder<VB, T>>(
    DiffUtilCallback()
) {

    protected var onItemClick: ((position: Int, data: T) -> Unit)? = null

    open fun initItem(onItemClick: ((position: Int, data: T) -> Unit)? = null): BaseListAdapter<VB, T> {
        this.onItemClick = onItemClick
        return this
    }

    inner class ItemViewHolder<VB : ViewDataBinding, T : Any?>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: T?) {
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<VB, T>, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<VB, T> {
        val binding = DataBindingUtil.inflate<VB>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return ItemViewHolder(binding)
    }

    class DiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return newItem === oldItem
        }
    }

    companion object {
        fun RecyclerView.placeholder(isGrid: Boolean = false) {
            adapter = if (isGrid) {
                BaseListAdapter<ItemPlaceholderGridBinding, Any>(R.layout.item_placeholder_grid)
            } else {
                BaseListAdapter<ItemPlaceholderBinding, Any>(R.layout.item_placeholder)
            }.apply {
                submitList(listOf(1, 2, 3, 4))
            }
        }
    }
}