package com.example.veganapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.veganapp.data.getRestaurantMocks
import com.example.veganapp.model.Restaurant
import kotlinx.android.synthetic.main.activity_favourite.*


class FavouriteActivity : AppCompatActivity() {
    private lateinit var restaurantList: ArrayList<Restaurant>
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        val actionBar = supportActionBar

        actionBar!!.title = "Ulubione"
        actionBar.setDisplayHomeAsUpEnabled(true)

        restaurantList = getRestaurantMocks()

        layoutManager = LinearLayoutManager(this)
        restaurantListRecyclerView.layoutManager = layoutManager
        restaurantListRecyclerView.adapter = RecyclerViewAdapter(this, restaurantList)
        restaurantListRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
