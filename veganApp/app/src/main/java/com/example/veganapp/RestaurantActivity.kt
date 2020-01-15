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
        restaurantHoursTextView.text = restaurant.hours
        ratingBar.rating = 4.5F
        menuExpandableListView.adapter

        expandableListView = findViewById(R.id.menuExpandableListView)
        if (expandableListView != null && restaurantNameTextView.text=="Restauracja Ratuszova") {
            val listData = HashMap<String, List<String>>()
            listData.put("Menu", arrayListOf<String>("Pierogi z Kaczką", "Gicz jagnięca", "Tatar z polędwicy wołowej","Krem z kiszonych ogórków"))
            listData.put("Opinie", arrayListOf<String>("Najlepsza restauracja w Poznaniu. Tradycyjna i przyjemna atmosfera. Polecam", "Restauracja ze świetną obsługą i jedzeniem. Tatar wołowy nie z tej ziemi"))
            val titleList = arrayListOf<String>("Menu", "Opinie")
            val adapter = MenuExpandableListViewAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
        }

        if (expandableListView != null && restaurantNameTextView.text=="Matii Sushi") {
            val listData = HashMap<String, List<String>>()
            listData.put("Menu", arrayListOf<String>("Nigiri set", "Osaka", "Mitsuro","Yoko+"))
            listData.put("Opinie", arrayListOf<String>("Great place with great Japanese food", "Smaczne jedzenie, miły personel i piękna restauracja.Polecam" +
                    ""))
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
