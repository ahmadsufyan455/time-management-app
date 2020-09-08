package com.fynzero.timemanagement.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.adapter.ItemAdapter
import com.fynzero.timemanagement.db.ActivityHelper
import com.fynzero.timemanagement.helper.MappingHelper
import com.fynzero.timemanagement.model.Activities
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var activityHelper: ActivityHelper
    private val activities = ArrayList<Activities>()
    private val itemAdapter = ItemAdapter(activities)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_history.layoutManager = LinearLayoutManager(activity)
        rv_history.setHasFixedSize(true)
        itemAdapter.notifyDataSetChanged()
        rv_history.adapter = itemAdapter

        // open database
        activityHelper = ActivityHelper.getInstance(requireActivity())
        activityHelper.open()

        // load history data
        loadData()

        btn_delete_all.setOnClickListener {
            val dialog = AlertDialog.Builder(requireActivity())
            dialog.setTitle(getString(R.string.delete_title))
            dialog.setMessage(getString(R.string.delete_message))
            dialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
                activityHelper.deleteAll()
                Toast.makeText(activity, "All activity was deleted", Toast.LENGTH_SHORT).show()
                activity?.recreate()
            }
            dialog.setNegativeButton(getString(R.string.no)) { _, _ ->
                // nothing
            }

            dialog.create().show()
        }

        // set item click
        itemAdapter.setOnItemClickCallback(object : ItemAdapter.OnItemClickCallback {
            override fun onItemClicked(activities: Activities) {
                val dialog = Dialog(requireActivity())
                dialog.setContentView(R.layout.data_dialog)

                // init component
                val activityName = dialog.findViewById<TextView>(R.id.txt_activity_name_dialog)
                val type = dialog.findViewById<TextView>(R.id.txt_activity_type_dialog)
                val startAt = dialog.findViewById<TextView>(R.id.txt_start_at_dialog)
                val endAt = dialog.findViewById<TextView>(R.id.txt_end_at_dialog)
                val duration = dialog.findViewById<TextView>(R.id.txt_duration_dialog)
                val btnCLose = dialog.findViewById<TextView>(R.id.txt_close)

                // instance
                activityName.text = activities.activities
                type.text = activities.type
                startAt.text = activities.startAt
                endAt.text = activities.endAt
                duration.text = activities.duration

                btnCLose.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

        })
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredHistory = async(Dispatchers.IO) {
                val cursor = activityHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val activities = deferredHistory.await()
            if (activities.size > 0) {
                itemAdapter.setData(activities)
                rv_history.visibility = View.VISIBLE
                img_no_activity.visibility = View.GONE
                txt_no_activity.visibility = View.GONE
            } else {
                rv_history.visibility = View.GONE
                img_no_activity.visibility = View.VISIBLE
                txt_no_activity.visibility = View.VISIBLE
            }
        }
    }
}