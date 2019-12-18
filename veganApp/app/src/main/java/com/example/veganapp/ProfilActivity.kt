package com.example.veganapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        val actionBar = supportActionBar

        actionBar!!.title = "Moja dieta"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
