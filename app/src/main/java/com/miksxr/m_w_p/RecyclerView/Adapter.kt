package com.miksxr.m_w_p.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.ListItemBinding

data class Model (
    val image: Int,
    val name: String,
    val rating: String,
    val games: String,
    val victories: String
)

class Adapter(private val listener: Listener) : ListAdapter<Model, Adapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ListItemBinding.bind(view)

        fun bind(item: Model, listener: Listener) = with(binding) {
            IV.setImageResource(item.image)
            TVname.setText(item.name)
            TVrating.setText(item.rating)
            TVgames.text = item.games

            val percentage = item.victories
            val percentageValue = if (percentage.isNotEmpty()) {
                percentage.split("%")[0].toIntOrNull() ?: 0
            } else {
                0
            }
            TVvictories.text = percentage

            if (percentageValue >= 50) {
                TVvictories.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else if (percentageValue >= 40) {
                TVvictories.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
            } else {
                TVvictories.text = "<40%"
            }

            itemView.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Model>() {
        override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener {
        fun onClick(item: Model)
    }
}