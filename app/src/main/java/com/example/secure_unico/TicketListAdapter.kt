package com.example.secure_unico

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secure_unico.network.Ticket
import com.google.android.material.card.MaterialCardView
import com.example.secure_unico.databinding.ListItemBinding

class TicketListAdapter(val clickListner: TicketListener): ListAdapter<Ticket,
        TicketListAdapter.TicketViewHolder>(DiffCallback) {

            class TicketViewHolder(private var binding:
            ListItemBinding): RecyclerView.ViewHolder(binding.root) {
                fun bind(clickListner: TicketListener,Ticket: Ticket) {
                    binding.ticket = Ticket
                    binding.clickListner = clickListner
                    binding.executePendingBindings()
                }
            }

    companion object DiffCallback: DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.qrcode == newItem.qrcode
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TicketViewHolder(
            ListItemBinding.inflate(layoutInflater,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = getItem(position)
        holder.bind(clickListner,ticket)
    }


}

class TicketListener(val clickListner: (ticket:Ticket) -> Unit) {
    fun onClick(ticket:Ticket) = clickListner(ticket)
}


