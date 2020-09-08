package com.fynzero.timemanagement.view.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateUtils
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.db.ActivityHelper
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DATE
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION_FORMAT
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.NAME
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_END
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_START
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TYPE
import com.fynzero.timemanagement.model.Activities
import kotlinx.android.synthetic.main.activity_progress.*
import java.text.SimpleDateFormat
import java.util.*

class ProgressActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PROGRESS = "extra_progress"
    }

    private lateinit var activityHelper: ActivityHelper
    private lateinit var simpleDateFormat: SimpleDateFormat
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var seconds: Long = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val activities = intent.getParcelableExtra<Activities>(EXTRA_PROGRESS)
        txt_activity_name.text = activities?.activities
        txt_activity_type.text = activities?.type

        // set activity helper
        activityHelper = ActivityHelper.getInstance(applicationContext)
        activityHelper.open()

        // start timer
        startTime = System.currentTimeMillis()
        simpleDateFormat = SimpleDateFormat("hh:mm:ss")
        val str = Date(startTime)
        val start = simpleDateFormat.format(str)
        activities?.startAt = start
        txt_duration.base = SystemClock.elapsedRealtime()
        txt_duration.start()
        txt_start_at.text = activities?.startAt

        // end timer
        btn_end_activity.setOnClickListener {
            endTime = System.currentTimeMillis()
            simpleDateFormat = SimpleDateFormat("hh:mm:ss")
            val end = Date(endTime)
            val stop = simpleDateFormat.format(end)
            txt_duration.stop()
            txt_duration.text = getString(R.string.timer)
            activities?.endAt = stop
            seconds = (endTime - startTime) / 1000
            val durationFormat = DateUtils.formatElapsedTime(seconds)
            activities?.durationFormat = durationFormat
            activities?.duration = seconds.toString() //duration
            Toast.makeText(this, getString(R.string.activity_has_end), Toast.LENGTH_SHORT).show()

            // insert data
            val values = ContentValues()
            values.put(NAME, activities?.activities)
            values.put(TYPE, activities?.type)
            values.put(TIME_START, activities?.startAt)
            values.put(TIME_END, activities?.endAt)
            values.put(DURATION, activities?.duration)
            values.put(DATE, activities?.date)
            values.put(DURATION_FORMAT, activities?.durationFormat)
            activityHelper.insert(values)

            // end activity progress
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun exitDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.exit))
        dialog.setMessage(getString(R.string.exit_message))
        dialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            finish()
        }
        dialog.setNegativeButton(getString(R.string.no)) { _, _ -> // keep up progress }
        }
        dialog.create().show()
    }
}