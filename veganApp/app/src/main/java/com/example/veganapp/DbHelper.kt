package com.example.veganapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.veganapp.model.Allergen
import com.example.veganapp.model.Diet
import com.example.veganapp.model.User
import org.json.JSONArray
import org.json.JSONObject
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
        private const val COLUMN_USERS_DIETS = "diets"
        private const val COLUMN_USERS_SYNCHRONIZED = "synchronized"

    }

    private val CREATE_USERS_TABLE = """CREATE TABLE $TABLE_USERS(
                                            $COLUMN_USERS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                            $COLUMN_USERS_USERNAME TEXT NOT NULL UNIQUE,
                                            $COLUMN_USERS_PASSWORD TEXT NOT NULL,
                                            $COLUMN_USERS_DIETS TEXT NOT NULL,
                                            $COLUMN_USERS_SYNCHRONIZED INTEGER NOT NULL
                                            );"""


    private val DROP_USERS_TABLE = "DROP TABLE IF EXISTS $TABLE_USERS"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
        db?.execSQL(CREATE_USERS_TABLE)
        db?.execSQL(
            "INSERT INTO $TABLE_USERS" +
                    "($COLUMN_USERS_USERNAME, $COLUMN_USERS_PASSWORD, $COLUMN_USERS_DIETS, $COLUMN_USERS_SYNCHRONIZED)" +
                    " VALUES ('Niezalogowany', 'Niezalogowany', '', 1)"
        )
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
        values.put(COLUMN_USERS_DIETS, "")
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

    fun getUserDiets(username: String): ArrayList<Diet> {
        val dietList: ArrayList<Diet> = ArrayList()

        val sql = """
            SELECT $COLUMN_USERS_DIETS
            FROM $TABLE_USERS
            WHERE $COLUMN_USERS_USERNAME=?;
            """

        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf(username))


        var dietsJSONString: String = ""

        if (cursor.moveToFirst()) {
            dietsJSONString = cursor.getString(0)
        }
        cursor.close()
        db.close()

        if (dietsJSONString.equals("")) {
            return dietList
        } else {
            val jsonArray = JSONArray(dietsJSONString)
            for (i in 0 until jsonArray.length()) {
                val dietName = jsonArray.getJSONObject(i).getString("name")
                val jsonAllergens = jsonArray.getJSONObject(i).getJSONArray("allergens")
                val allergenList: ArrayList<Allergen> = ArrayList()
                val diet = Diet(dietName)
                for (i in 0 until jsonAllergens.length()) {
                    val jsonAllergen = jsonAllergens.getJSONObject(i)
                    diet.allergens.add(Allergen(jsonAllergen.getString("name"), jsonAllergen.getBoolean("state")))
                }
                dietList.add(diet)
            }
            return dietList
        }
    }

    private fun dietToJSONObject(diet: Diet): JSONObject {
        val jsonDiet = JSONObject()
        jsonDiet.put("name", diet.name)
        val jsonAllergens = JSONArray()
        diet.allergens.forEach {
            val jsonAllergen = JSONObject()
            jsonAllergen.put("name", it.name)
            jsonAllergen.put("state", it.state)
            jsonAllergens.put(jsonAllergen)
        }
        jsonDiet.put("allergens", jsonAllergens)
        return jsonDiet
    }

    fun addDiet(username: String, dietName: String, allergenList: ArrayList<Allergen>) {
        val jsonDiets = JSONArray()
        getUserDiets(username).forEach {
            jsonDiets.put(dietToJSONObject(it))
        }

        val newDiet = Diet(dietName)
        newDiet.allergens.addAll(allergenList)
        jsonDiets.put(dietToJSONObject(newDiet))

        Log.d("XDXDXD", jsonDiets.toString())
        updateUserDiets(username, jsonDiets.toString())
    }

    private fun updateUserDiets(username: String, jsonString: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERS_DIETS, jsonString)

        val retVal = db.update(TABLE_USERS, values, "$COLUMN_USERS_USERNAME = ?", arrayOf(username))
        if (retVal >= 1) {
            Log.d("XDXDXD", " Record updated")
        } else {
            Log.d("XDXDXD", " Not updated")
        }
        db.close()

    }

}