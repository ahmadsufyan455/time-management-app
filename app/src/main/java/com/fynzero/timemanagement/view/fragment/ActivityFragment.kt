package com.fynzero.timemanagement.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.adapter.ItemAdapter
import com.fynzero.timemanagement.db.ActivityHelper
import com.fynzero.timemanagement.helper.MappingHelper
import com.fynzero.timemanagement.helper.UserPref
import com.fynzero.timemanagement.model.Activities
import com.fynzero.timemanagement.view.activity.ProgressActivity
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityFragment : Fragment() {

    private lateinit var activityHelper: ActivityHelper
    private lateinit var userPref: UserPref
    private val activities = ArrayList<Activities>()
    private val itemAdapter = ItemAdapter(activities)
    private val date = SimpleDateFormat("MMM dd, YYYY", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set greeting card by name and time
        userPref = UserPref(requireActivity())
        val name = userPref.getName(UserPref.PREF_USERNAME)
        txt_name.text = name
        greetByTime()
        txt_date.text = date

        // get data
        loadActivityAsync()

        // setup recycler view
        rv_activity.layoutManager = LinearLayoutManager(activity)
        rv_activity.setHasFixedSize(true)
        itemAdapter.notifyDataSetChanged()
        rv_activity.adapter = itemAdapter

        // open database
        activityHelper = ActivityHelper.getInstance(requireActivity())
        activityHelper.open()

        // set button add clicked
        btn_add_activity.setOnClickListener {
            addDialog()
        }

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
                duration.text = activities.durationFormat

                btnCLose.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

        })

    }

    // set time to display greet
    private fun greetByTime() {
        val calendar = Calendar.getInstance()
        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> txt_greet.text = getString(R.string.greet)
            in 12..15 -> txt_greet.text = getString(R.string.greet_afternoon)
            in 16..20 -> txt_greet.text = getString(R.string.greet_evening)
            in 21..23 -> txt_greet.text = getString(R.string.greet_night)
        }
    }

    // create dialog builder
    private fun addDialog() {
        val activities = Activities()
        // set dialog
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.add_dialog)

        // init component
        val btnAdd = dialog.findViewById<TextView>(R.id.txt_add)
        val btnCancel = dialog.findViewById<TextView>(R.id.txt_cancel)
        val edtActivities = dialog.findViewById<EditText>(R.id.edt_input_activity)
        val rgType = dialog.findViewById<RadioGroup>(R.id.rg_type)
        val rbSoft = dialog.findViewById<RadioButton>(R.id.rb_soft)
        val rbMedium = dialog.findViewById<RadioButton>(R.id.rb_medium)
        val rbHard = dialog.findViewById<RadioButton>(R.id.rb_hard)

        btnAdd.setOnClickListener {
            var value = getString(R.string.soft)
            when (rgType.checkedRadioButtonId) {
                rbSoft.id -> value = getString(R.string.soft)
                rbMedium.id -> value = getString(R.string.medium)
                rbHard.id -> value = getString(R.string.hard)
            }
            activities.type = value
            activities.activities = edtActivities.text.toString()
            activities.date = date

            if (edtActivities.text.toString().isNotEmpty()) {
                val intent = Intent(activity, ProgressActivity::class.java)
                intent.putExtra(ProgressActivity.EXTRA_PROGRESS, activities)
                startActivity(intent)
                activity?.finish()
                dialog.dismiss()
            } else {
                edtActivities.error = getString(R.string.is_empty)
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun loadActivityAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredActivity = async(Dispatchers.IO) {
                val cursor = activityHelper.queryByDate(date)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val activities = deferredActivity.await()
            if (activities.size > 0) {
                itemAdapter.setData(activities)
                rv_activity.visibility = View.VISIBLE
                txt_no_activity.visibility = View.GONE
                img_no_activity.visibility = View.GONE
            } else {
                rv_activity.visibility = View.GONE
                txt_no_activity.visibility = View.VISIBLE
                img_no_activity.visibility = View.VISIBLE
            }
        }
    }
}