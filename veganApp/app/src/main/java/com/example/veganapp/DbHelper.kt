package com.example.veganapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.veganapp.model.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "QuickNotes.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "Users"
        private const val COLUMN_USERS_ID = "id"
        private const val COLUMN_USERS_USERNAME = "username"
        private const val COLUMN_USERS_PASSWORD = "password"
        private const val COLUMN_USERS_SYNCHRONIZED = "synchronized"

    }

    private val CREATE_USERS_TABLE = """CREATE TABLE $TABLE_USERS(
                                            $COLUMN_USERS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                            $COLUMN_USERS_USERNAME TEXT NOT NULL UNIQUE,
                                            $COLUMN_USERS_PASSWORD TEXT NOT NULL,
                                            $COLUMN_USERS_SYNCHRONIZED INTEGER NOT NULL
                                            );"""


    private val DROP_USERS_TABLE = "DROP TABLE IF EXISTS $TABLE_USERS"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
        db?.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_USERS_TABLE)
        onCreate(db)
    }

    fun userExistsInLocalDatabase(username: String): Boolean {
        val db = this.readableDatabase

        val selection = "$COLUMN_USERS_USERNAME = ?"
        val selectionArgs = arrayOf(username)

        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USERS_USERNAME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        if (cursor.count > 0)
            return true
        return false
    }

    fun addUser(serverAddress: String, username: String, password: String): Boolean {
        if (postUser(serverAddress, username, password)) {
            addUserToLocalDatabase(username, password)
            return true
        }
        return false
    }

    private fun addUserToLocalDatabase(username: String, password: String): Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USERS_USERNAME, username)
        values.put(COLUMN_USERS_PASSWORD, password)
        values.put(COLUMN_USERS_SYNCHRONIZED, 1)

        val status = db.insert(TABLE_USERS, null, values)
        db.close()

        if (status < 0)
            return false
        return true
    }


    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase

        val selection = "$COLUMN_USERS_USERNAME = ? AND $COLUMN_USERS_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USERS_USERNAME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }
        return false
    }

    fun synchronizeUsers(users: ArrayList<User>) {
        users.forEach {
            if (!userExistsInLocalDatabase(it.username)) {
                addUserToLocalDatabase(it.username, it.password)
            }
        }
    }

    private fun postUser(serverAddress: String, username: String, password: String): Boolean {
        if (!isServerReachable(serverAddress))
            return false

        var reqParam =
            URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(
            password,
            "UTF-8"
        )
        val mURL = URL(serverAddress + "register")

        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"

            val wr = OutputStreamWriter(outputStream)
            wr.write(reqParam)
            wr.flush()

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                if (response.toString() == "ok")
                    return true
            }
        }
        return false
    }

}