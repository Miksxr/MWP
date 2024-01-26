package com.miksxr.m_w_p.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.ListItem2Binding

data class Model2 (
    val image: Int,
    val games: String,
    val victories: String
)

class Adapter2 : ListAdapter<Model2, Adapter2.Holder2>(Comparator()) {

    class Holder2(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListItem2Binding.bind(view)
        fun bind(item: Model2) = with(binding) {
            IV.setImageResource(item.image)
            TVgames.text = item.games

            val percentage = item.victories
            val percentageValue = percentage.split("%")[0].toInt()
            TVvictories.text = percentage

            if (percentageValue > 50) {
                TVvictories.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else {
                TVvictories.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Model2>() {
        override fun areItemsTheSame(oldItem: Model2, newItem: Model2): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Model2, newItem: Model2): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item2, parent, false)
        return Holder2(view)
    }

    override fun onBindViewHolder(holder: Holder2, position: Int) {
        holder.bind(getItem(position))
    }
}