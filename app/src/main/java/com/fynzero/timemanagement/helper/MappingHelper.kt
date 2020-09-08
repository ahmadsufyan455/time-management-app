package com.fynzero.timemanagement.helper

import android.database.Cursor
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DATE
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION_FORMAT
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.NAME
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_END
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_START
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TYPE
import com.fynzero.timemanagement.model.Activities

object MappingHelper {
    fun mapCursorToArrayList(activityCursor: Cursor): ArrayList<Activities> {
        val activities = ArrayList<Activities>()

        activityCursor.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(NAME))
                val type = getString(getColumnIndexOrThrow(TYPE))
                val timeStart = getString(getColumnIndexOrThrow(TIME_START))
                val timeEnd = getString(getColumnIndexOrThrow(TIME_END))
                val duration = getString(getColumnIndexOrThrow(DURATION))
                val date = getString(getColumnIndexOrThrow(DATE))
                val durationFormat = getString(getColumnIndexOrThrow(DURATION_FORMAT))
                activities.add(Activities(0, name, type, timeStart, timeEnd, duration, date, durationFormat))
            }
        }
        return activities
    }
}