package com.example.secure_unico

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secure_unico.model.SealedApiStatus
import com.example.secure_unico.network.Ticket




@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
data: List<Ticket>?) {
    val adapter = recyclerView.adapter as TicketListAdapter
    adapter.submitList(data)
}

@BindingAdapter("sealedApiStatus")
fun bindStatus(statusImageView: ImageView, status: SealedApiStatus?) {
    when(status) {
        SealedApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        else -> {
            statusImageView.visibility = View.GONE

        }
    }
}



