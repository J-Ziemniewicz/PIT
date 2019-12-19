package com.example.veganapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import android.widget.ExpandableListView
import com.example.veganapp.data.getRestaurantMocks
import com.example.veganapp.model.Restaurant
import kotlinx.android.synthetic.main.activity_restaurant.*
import java.io.File

class RestaurantActivity : AppCompatActivity() {
    private lateinit var expandableListView : ExpandableListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        val restaurant: Restaurant = getRestaurantMocks()[intent.getIntExtra("position", 0)]
        showImage(restaurant)
        restaurantNameTextView.text = restaurant.name
        restaurantTypeTextView.text = restaurant.type
        restaurantAddressTextView.text = restaurant.address
        restaurantHoursTextView.text = "Pn-Pt: 8 - 21\nSoboty: 10 - 21\nNiedziele: 10 - 20"
        ratingBar.rating = 4.5F
        menuExpandableListView.adapter

        expandableListView = findViewById(R.id.menuExpandableListView)
        if (expandableListView != null) {
            val listData = HashMap<String, List<String>>()
            listData.put("Menu", arrayListOf<String>("wypisane dania\n z alergenami\n", "kolejne danie", "itd\n\n...\n"))
            listData.put("Opinie", arrayListOf<String>("bardzo fajna knajpa", "smacznie i niedrogo"))
            val titleList = arrayListOf<String>("Menu", "Opinie")
            val adapter = MenuExpandableListViewAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

        }

    }

    private fun showImage(restaurant: Restaurant) {
        val dir = "Pictures/"
        val pathName =
            getExternalStorageDirectory().toString() + File.separator + dir + restaurant.imagePath
        Log.d("XDXDXD", pathName)

        if (File(pathName).exists()) {
            val myBitmap = BitmapFactory.decodeFile(pathName)
            if (myBitmap != null) {
                restaurantImageView.setImageBitmap(myBitmap)
            }
        }
    }
}
