package com.example.veganapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veganapp.data.getRestaurantMocks
import com.example.veganapp.model.Restaurant
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_favourite.*


class FavouriteActivity : AppCompatActivity() {
    private lateinit var restaurantList: ArrayList<Restaurant>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        val actionBar = supportActionBar

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        actionBar!!.title = "Ulubione"
        actionBar.setDisplayHomeAsUpEnabled(true)

        restaurantList = getRestaurantMocks()

        layoutManager = LinearLayoutManager(this)
        restaurantListRecyclerView.layoutManager = layoutManager
        restaurantListRecyclerView.adapter = RecyclerViewAdapter(this, restaurantList)
        restaurantListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
