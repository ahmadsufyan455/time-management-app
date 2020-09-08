package com.fynzero.timemanagement.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DATE
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DURATION_FORMAT
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.NAME
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TABLE_NAME
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_END
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TIME_START
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TYPE
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "timeManagement"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_ACTIVITY =
            "CREATE TABLE $TABLE_NAME (${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $NAME TEXT NOT NULL, $TYPE TEXT NOT NULL," +
                    " $TIME_START TEXT NOT NULL, $TIME_END TEXT NOT NULL," +
                    " $DURATION TEXT NOT NULL, $DATE TEXT NOT NULL, $DURATION_FORMAT TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_ACTIVITY)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}