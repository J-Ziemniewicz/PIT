
package com.example.veganapp

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veganapp.data.getRestaurantMocks
import com.example.veganapp.model.Restaurant
import kotlinx.android.synthetic.main.activity_favourite.*


class Search : AppCompatActivity() {
    private lateinit var restaurantList: ArrayList<Restaurant>
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val actionBar = supportActionBar

        actionBar!!.title = "Wyszukaj lokal"
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu,menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
