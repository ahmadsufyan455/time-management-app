package com.fynzero.timemanagement.helper

import android.database.Cursor
import com.fynzero.timemanagement.db.DatabaseContract
import com.fynzero.timemanagement.model.Activities

object MappingDateHelper {
    fun mapCursorToArrayList(activityCursor: Cursor): ArrayList<Activities> {
        val activities = ArrayList<Activities>()

        activityCursor.apply {
            while (moveToNext()) {
                val date = getString(getColumnIndexOrThrow(DatabaseContract.ActivityColumns.DATE))
                activities.add(Activities(0, null, null, null, null, null, date, null))
            }
        }
        return activities
    }
}