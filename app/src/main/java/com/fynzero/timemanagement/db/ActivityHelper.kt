package com.fynzero.timemanagement.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.DATE
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion.TABLE_NAME
import com.fynzero.timemanagement.db.DatabaseContract.ActivityColumns.Companion._ID
import java.sql.SQLException

class ActivityHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: ActivityHelper? = null
        fun getInstance(context: Context): ActivityHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ActivityHelper(context)
        }

        private lateinit var database: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    // get all data
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID DESC"
        )
    }

    // read date
    fun queryDate(): Cursor {
        val query = "SELECT DISTINCT $DATE FROM $TABLE_NAME ORDER BY $_ID DESC"
        return database.rawQuery(query, null)
    }

    // query by date
    fun queryByDate(date: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$DATE = ?",
            arrayOf(date),
            null,
            null,
            "$_ID DESC",
            null
        )
    }

    // insert / add data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // delete all data
    fun deleteAll() {
        database.delete(DATABASE_TABLE, null, null)
    }
}