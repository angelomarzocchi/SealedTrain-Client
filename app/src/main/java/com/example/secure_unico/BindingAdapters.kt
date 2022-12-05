package com.example.secure_unico

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secure_unico.network.Ticket

class BindingAdapters


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
data: List<Ticket>?) {
    val adapter = recyclerView.adapter as TicketListAdapter
    adapter.submitList(data)
}

