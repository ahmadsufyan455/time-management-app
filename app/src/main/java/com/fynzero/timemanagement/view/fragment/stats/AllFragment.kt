package com.fynzero.timemanagement.view.fragment.stats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.adapter.StatsAdapter
import com.fynzero.timemanagement.db.ActivityHelper
import com.fynzero.timemanagement.helper.MappingDateHelper
import com.fynzero.timemanagement.model.Activities
import com.fynzero.timemanagement.view.activity.StatsActivity
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AllFragment : Fragment() {

    private val dates = ArrayList<Activities>()
    private val statsAdapter = StatsAdapter(dates)

    private lateinit var activityHelper: ActivityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // open database
        activityHelper = ActivityHelper.getInstance(requireActivity())
        activityHelper.open()

        // store date
        storeDate()

        // setup recyclerview
        rv_all_stats.layoutManager = LinearLayoutManager(activity)
        rv_all_stats.setHasFixedSize(true)
        statsAdapter.notifyDataSetChanged()
        rv_all_stats.adapter = statsAdapter

        statsAdapter.setOnItemClickCallBack(object : StatsAdapter.OnItemClickCallBack {
            override fun onItemClicked(activities: Activities) {
                val intent = Intent(activity, StatsActivity::class.java)
                intent.putExtra(StatsActivity.EXTRA_DATE, activities)
                startActivity(intent)
            }
        })
    }

    private fun storeDate() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredActivity = async(Dispatchers.IO) {
                val cursor = activityHelper.queryDate()
                MappingDateHelper.mapCursorToArrayList(cursor)
            }
            val activities = deferredActivity.await()
            if (activities.size > 0) {
                statsAdapter.setDate(activities)
            }
        }
    }
}