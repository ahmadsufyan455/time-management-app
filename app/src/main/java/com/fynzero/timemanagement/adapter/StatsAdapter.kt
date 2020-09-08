package com.fynzero.timemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.model.Activities
import kotlinx.android.synthetic.main.item_stats.view.*

class StatsAdapter(private val dates: ArrayList<Activities>) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setDate(date: ArrayList<Activities>) {
        dates.clear()
        dates.addAll(date)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stats, parent, false)
        ))
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dates[position])
        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(dates[holder.adapterPosition])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(activities: Activities) {
            with(itemView) {
                txt_date_stats.text = activities.date
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(activities: Activities)
    }
}