package com.example.veganapp

import android.content.Context
import android.net.ConnectivityManager
import com.example.veganapp.model.User
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import java.lang.Exception
import java.net.*


fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    if (networkInfo != null && networkInfo.isConnected)
        return true
    return false
}

fun isServerReachable(serverAddress: String): Boolean {
    val url = URL(serverAddress)
    return try {
        val socket = Socket()
        socket.soTimeout = 2000
        socket.connect(InetSocketAddress(url.host, url.port), 2000)
        socket.close()
        true
    } catch (e: Exception) {
        false
    }
}

fun parseUsers(json: String): ArrayList<User> {
    val users = ArrayList<User>()

    val jsonArray = JSONArray(json)
    for (i in 0 until jsonArray.length()) {
        val username = jsonArray.getJSONObject(i).getString("username")
        val password = jsonArray.getJSONObject(i).getString("password")
        users.add(User(username, password))
    }
    return users
}

fun saveServerAddress(context: Context, serverAddress: String) {
    val sharedPreference =
        context.getSharedPreferences("com.example.veganapp.prefs", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString("server_address", serverAddress)
    editor.apply()
}

fun getServerAddress(context: Context): String {
    val sharedPreferences =
        context.getSharedPreferences("com.example.veganapp.prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("server_address", "http://192.168.0.105:3000/")
        ?: return "http://192.168.0.105:3000/"
}

fun getCurrentUser(context: Context): String {
    val sharedPreferences =
        context.getSharedPreferences("com.example.veganapp.prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("username", "Niezalogowany")
        ?: return "Niezalogowany"
}

fun saveCurrentUser(context: Context, username: String, password: String) {
    val sharedPreference =
        context.getSharedPreferences("com.example.veganapp.prefs", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString("username", username)
    editor.putString("password", password)
    editor.apply()
}

fun synchronizeWithServerAsync(context: Context) {
    val serverAddress = getServerAddress(context)
    context.doAsync {
        val result = URL(serverAddress + "all_users").readText()
        val usernames = parseUsers(result)
        val dbHelper = DbHelper(context)
        dbHelper.synchronizeUsers(usernames)

        uiThread {
            context.toast("Synchronizacja zakończona")
        }
    }
}