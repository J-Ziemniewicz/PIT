package com.example.veganapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.veganapp.model.Diet
import kotlinx.android.synthetic.main.activity_diet_list.*
import kotlinx.android.synthetic.main.content_diet_list.*
import org.jetbrains.anko.startActivity

class DietList : AppCompatActivity() {
    private val dietList: ArrayList<Diet> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_list)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dietList.addAll(DbHelper(applicationContext).getUserDiets(getCurrentUser(applicationContext)))

        layoutManager = LinearLayoutManager(this)
        dietListRecyclerView.layoutManager = layoutManager
        dietListRecyclerView.adapter = DietListRecyclerViewAdapter(this, dietList)
        dietListRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        fab.setOnClickListener { view ->
            startActivity<NewDiet>()
        }
    }

    override fun onResume() {
        super.onResume()
        dietList.clear()
        dietList.addAll(DbHelper(applicationContext).getUserDiets(getCurrentUser(applicationContext)))
        dietListRecyclerView.adapter?.notifyDataSetChanged()
    }
}
