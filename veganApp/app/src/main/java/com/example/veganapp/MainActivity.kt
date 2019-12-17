package com.example.veganapp

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavigationView: NavigationView
    private lateinit var sMapFragment : SupportMapFragment
    private lateinit var lastLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sMapFragment = SupportMapFragment.newInstance()
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener { item ->
            navigationItemSelectedHandler(item)
        }

        val headerView: View = mNavigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.usernameTextView)
        navUsername.text = getCurrentUser(applicationContext)
        sMapFragment.getMapAsync(this);
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.map, sMapFragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
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
        mDrawerLayout.openDrawer(Gravity.START)
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

    override fun onMapReady(p0: GoogleMap?) {

    }
}
