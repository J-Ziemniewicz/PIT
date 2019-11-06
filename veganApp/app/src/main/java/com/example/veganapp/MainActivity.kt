package com.example.veganapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.View

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {

            }
            R.id.nav_favourite -> {

            }
            R.id.nav_opinion -> {

            }

            R.id.nav_share -> {

            }
            R.id.nav_settings -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun openMenu(view: View){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        drawerLayout.openDrawer(Gravity.START)
    }

    fun profileClick(menuItem: MenuItem){
        val thread = Thread {
            run{
                val intent = Intent(this, ProfilActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    fun favouriteClick(menuItem: MenuItem){
        val thread = Thread {
            run{
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    fun opinionClick(menuItem: MenuItem){
        val thread = Thread {
            run{
                val intent = Intent(this, OpinionActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    fun shareClick(menuItem: MenuItem){
        val thread = Thread {
            run{
                val intent = Intent(this, ShareActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }

    fun setClick(menuItem: MenuItem){
        val thread = Thread {
            run{
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        thread.start()
    }
}
