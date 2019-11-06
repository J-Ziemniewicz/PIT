package com.example.veganapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class OpinionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opinion)
      val actionBar = supportActionBar

        actionBar!!.title = "Opinie"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}