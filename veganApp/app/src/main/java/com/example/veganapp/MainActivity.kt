package com.example.veganapp

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener

{
    private lateinit var mDrawerLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var mNavigationView: NavigationView
    private lateinit var sMapFragment: SupportMapFragment
    private lateinit var lastLocation: Location
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sMapFragment = SupportMapFragment.newInstance()
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up)
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener { item ->
            navigationItemSelectedHandler(item)
        }

        val headerView: View = mNavigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.usernameTextView)
        navUsername.text = getCurrentUser(applicationContext)
        sMapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.map, sMapFragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menuButton -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigationItemSelectedHandler(item: MenuItem): Boolean {
        item.isChecked = false // czemu to nie działa?
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers()
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                profileClick(item)
            }
            R.id.nav_favourite -> {
                favouriteClick(item)
            }
            R.id.nav_opinion -> {
                opinionClick(item)
            }

            R.id.nav_share -> {
                shareClick(item)
            }
            R.id.nav_settings -> {
                setClick(item)
            }
            R.id.nav_logout -> {
                logoutClick(item)
            }
        }
        return true
    }

    fun openMenu(view: View) {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    private fun profileClick(menuItem: MenuItem) {
        val thread = Thread {
            run {
                val intent = Intent(this, DietList::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    private fun favouriteClick(menuItem: MenuItem) {
        val thread = Thread {
            run {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    private fun opinionClick(menuItem: MenuItem) {
        val thread = Thread {
            run {
                val intent = Intent(this, OpinionActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    private fun shareClick(menuItem: MenuItem) {
        val thread = Thread {
            run {
                val intent = Intent(this, ShareActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    private fun setClick(menuItem: MenuItem) {
        val thread = Thread {
            run {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    private fun logoutClick(menuItem: MenuItem) {
        alert("Jesteś pewien?") {
            title = "Wylogowywanie"
            yesButton {
                saveCurrentUser(applicationContext, "", "")
                startActivity<LoginActivity>()
                finish()
            }
            noButton {}
        }.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val poznan = LatLng(52.4080, 16.9341)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(poznan, 14.0f))
        map.setOnMarkerClickListener(this)
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isZoomControlsEnabled = false

        setUpMap()

        placeMarkerOnMap(LatLng(52.4075323,16.9340189), "Restauracja Ratuszova")
        placeMarkerOnMap(LatLng(52.4006427,16.9266153), "Matti Sushi")
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
            }
        }
    }

    override fun onMarkerClick(marker : Marker) : Boolean{
//        if (marker.title.equals("Restauracja Ratuszova"))
//        {
//            val thread = Thread {
//                run {
//                    val intent = Intent(this, ShareActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            thread.start()
//        }
        return false
    }



    private fun placeMarkerOnMap(location: LatLng, title: String) {
        // 1
        val markerOptions = MarkerOptions().position(location).title(title)
        // 2
        map.addMarker(markerOptions)
    }

    //TODO: Nie działa klikanie na okienko markera
    override fun onInfoWindowClick(marker: Marker) {
//        Toast.makeText(this, "Info window clicked",
//            Toast.LENGTH_SHORT).show();

        if (marker.title.equals("Restauracja Ratuszova"))
        {
            val thread = Thread {
                run {
                    val intent = Intent(this, ShareActivity::class.java)
                    startActivity(intent)
                }
            }
            thread.start()
        }
    }
}
