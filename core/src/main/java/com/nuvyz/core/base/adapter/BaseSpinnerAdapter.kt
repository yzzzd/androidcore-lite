package com.nuvyz.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.nuvyz.core.BR

class BaseSpinnerAdapter<VB: ViewDataBinding, T: Any>(@LayoutRes private val layoutRes: Int, private val data: ArrayList<T>) : BaseAdapter(), Filterable {

    private val filterThatDoesNothing = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = data
            results.count = data.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filterThatDoesNothing
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            val binding = DataBindingUtil.inflate<VB>(LayoutInflater.from(parent?.context), layoutRes, parent, false).apply {
                setVariable(BR.data, data[position])
                executePendingBindings()
            }
            return binding.root
        }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (data.isEmpty()) {
            0
        } else {
            data.size
        }
    }
}