package com.example.veganapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        val actionBar = supportActionBar

        actionBar!!.title = "Udostępnij"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
