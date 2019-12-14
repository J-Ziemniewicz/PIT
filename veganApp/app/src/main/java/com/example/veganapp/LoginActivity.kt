package com.example.veganapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.veganapp.DbHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DbHelper(applicationContext)

        signinOrRegisterButton.setOnClickListener { signinOrRegisterButtonListener() }
        continueWithoutLoginButton.setOnClickListener {
            startActivity<MainActivity>()
            saveCurrentUser(applicationContext, "Niezalogowany", "Niezalogowany")
        }
        signinButton.setOnClickListener { signin() }
        registerButton.setOnClickListener { register() }

        synchronizeWithServerAsync(applicationContext)

        if (checkUserLoggedIn()) {
            finish()
            startActivity<MainActivity>()
        }
    }


    private fun register() {
        Log.d("XDXDXD", "rejsetracja")
        if (checkInternetConnection(applicationContext)) {
            doAsync {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()
                Log.d("XDXDXD", "dodaje usera")
                val ok = dbHelper.addUser(getServerAddress(applicationContext), login, password)
                Log.d("XDXDXD", "udało sie")
                if (ok) {
                    Log.d("XDXDXD", "dodano poprawnie usera")
                    saveCurrentUser(applicationContext, login, password)
                    finish()
                    startActivity<MainActivity>()
                }
                uiThread {
                    if (ok) {
                        toast("Zarejestrowano")
                    } else {
                        toast("Błąd połączenia z serwerem")
                    }
                }
            }
        } else {
            alert("Rejestracja jest możliwa tylko w trybie online") {
                title = "Brak połączenia"
                yesButton {}
            }.show()
        }
    }

    private fun checkUserLoggedIn(): Boolean {
        val sharedPreferences =
            getSharedPreferences("com.example.veganapp.prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        if (username != null && username != "" && password != null && password != "")
            return true
        return false
    }

    private fun signinOrRegisterButtonListener() {
        if (loginEditText.text.isNotBlank()) {
            if (dbHelper.userExistsInLocalDatabase(loginEditText.text.toString())) {
                signinButton.visibility = View.VISIBLE
            } else {
                registerButton.visibility = View.VISIBLE
            }
            continueWithoutLoginButton.visibility = View.GONE
            signinOrRegisterButton.visibility = View.GONE
            loginEditText.isEnabled = false
            passwordEditText.visibility = View.VISIBLE

            passwordEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(passwordEditText, 0)

            val actionBar = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)

        } else {
            toast("Podaj login")
        }
    }

    private fun signin() {
        if (validatePassword()) {
            saveCurrentUser(
                applicationContext,
                loginEditText.text.toString(),
                passwordEditText.text.toString()
            )
            finish()
            startActivity<MainActivity>()
        }
    }

    private fun validatePassword(): Boolean {
        if (passwordEditText.text.isNotBlank()) {
            if (dbHelper.checkUser(loginEditText.text.toString(), passwordEditText.text.toString()))
                return true
            else {
                alert("Proszę podaj właściwe hasło") {
                    title = "Niepoprawne hasło"
                    yesButton {}
                }.show()
                return false
            }
        }
        toast("Podaj hasło")
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        startActivity(intent)
        return true
    }
}