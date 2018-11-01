package com.sporksoft.fitbodtest.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.ViewGroup
import android.view.View
import com.sporksoft.affirm.helper.extensions.inflate
import com.sporksoft.fitbodtest.R
import com.sporksoft.fitbodtest.model.FeedItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_feed.*

class FeedAdapter(val items: MutableList<FeedItem> = ArrayList(), val onClickListener: View.OnClickListener? = null): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_feed),
            onClickListener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item)
        holder.containerView.tag = position
    }

    class ViewHolder(override val containerView: View, listener: View.OnClickListener? = null) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        init {
            containerView.setOnClickListener(listener)
        }

        fun bind(item: FeedItem) {
            title.text = item.name
            maxWeight.text = item.max.toString()
        }
    }

    fun addPage(newItems: List<FeedItem>?) {
        if (newItems == null) {
            return
        }
        items.addAll(newItems)
        print("ITEMS: " + items)
        notifyDataSetChanged()
    }

    fun reset(newItems: List<FeedItem>?) {
        if (newItems == null) {
            return
        }
        items.clear()
        addPage(newItems)
    }

}