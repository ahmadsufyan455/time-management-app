package com.fynzero.timemanagement.view.activity

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.db.ActivityHelper
import com.fynzero.timemanagement.model.Activities
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATE = "date"
    }

    private lateinit var activityHelper: ActivityHelper
    private val names = ArrayList<String>()
    private val duration = ArrayList<String>()
    private val durationFormat = ArrayList<String>()
    private lateinit var date: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val activities = intent.getParcelableExtra<Activities>(EXTRA_DATE)
        date = activities?.date.toString()
        txt_activity_date.text = date

        btn_back.setOnClickListener { finish() }

        // open the database
        activityHelper = ActivityHelper.getInstance(this)
        activityHelper.open()

        // store data
        storeData()

        // convert string array to int array
        val results = duration.map { it.toInt() }.toIntArray()

        val pie = AnyChart.pie()
        val data = ArrayList<DataEntry>()
        for (i in results.indices) {
            data.add(ValueDataEntry(names[i], results[i]))
        }

        pie.data(data)
        pie_chart.setChart(pie)

        // set time usage
        val name = StringBuilder()
        for (i in names) {
            name.append("- ")
            name.append(i)
            name.append(" :")
            name.append("\n")
        }
        txt_time_usage_name.text = name

        val time = StringBuilder()
        for (i in durationFormat) {
            time.append(i)
            time.append("\n")
        }
        txt_time_usage_duration.text = time

        var total = 0
        for (i in results) {
            total += i
        }
        val totalFormat = DateUtils.formatElapsedTime(total.toLong())
        txt_time_total.text = "${getString(R.string.total)} $totalFormat"
    }

    private fun storeData() {
        val cursor: Cursor? = activityHelper.queryByDate(date)
        if (cursor?.count == 0) {
            pie_chart.visibility = View.GONE
            txt_time_usage_name.visibility = View.GONE
            txt_time_usage_duration.visibility = View.GONE
            label_usage.visibility = View.GONE
            img_stats.visibility = View.GONE
            txt_time_total.visibility = View.GONE
            img_no_stats.visibility = View.VISIBLE
            txt_no_data.visibility = View.VISIBLE
        } else {
            while (cursor?.moveToNext()!!) {
                names.add(cursor.getString(1))
                duration.add(cursor.getString(5))
                durationFormat.add(cursor.getString(7))
            }
            img_no_stats.visibility = View.GONE
            txt_no_data.visibility = View.GONE
            pie_chart.visibility = View.VISIBLE
            txt_time_usage_name.visibility = View.VISIBLE
            txt_time_usage_duration.visibility = View.VISIBLE
            label_usage.visibility = View.VISIBLE
            txt_time_total.visibility = View.VISIBLE
            img_stats.visibility = View.VISIBLE
        }
    }
}