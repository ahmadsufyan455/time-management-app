package com.fynzero.timemanagement.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class ActivityColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "activity"
            const val _ID = "_id"
            const val NAME = "activity_name"
            const val TYPE = "activity_type"
            const val TIME_START = "time_start"
            const val TIME_END = "time_end"
            const val DURATION = "duration"
            const val DATE = "date"
            const val DURATION_FORMAT = "duration_format"
        }
    }

}