package com.fynzero.timemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.model.Activities
import kotlinx.android.synthetic.main.item_activity.view.*

class ItemAdapter(private val activityList: ArrayList<Activities>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(list: ArrayList<Activities>) {
        activityList.clear()
        activityList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        ))
    }

    override fun getItemCount(): Int = activityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activityList[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(activityList[holder.adapterPosition])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(activities: Activities) {
            with(itemView) {
                txt_activity_name.text = activities.activities
                txt_duration.text = activities.durationFormat
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(activities: Activities)
    }
}