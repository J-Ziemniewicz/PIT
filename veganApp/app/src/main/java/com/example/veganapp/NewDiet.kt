package com.example.veganapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.veganapp.model.Allergen
import kotlinx.android.synthetic.main.activity_new_diet.*
import kotlinx.android.synthetic.main.allergen_choice.*

class NewDiet : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_diet)

        dietNameEditText.setOnFocusChangeListener { view, b -> hideKeyboard(view, b) }


        val allergenList = ArrayList<Allergen>()
        allergenList.add(Allergen("Mleko", false))
        allergenList.add(Allergen("Jaja", false))
        allergenList.add(Allergen("Gluten", false))
        allergenList.add(Allergen("Soja", false))
        allergenList.add(Allergen("Orzechy", false))
        allergenList.add(Allergen("Cytrusy", false))

        layoutManager = LinearLayoutManager(this)
        allergensRecycleView.layoutManager = layoutManager
        allergensRecycleView.adapter = AlergenRecycleViewAdapter(this.applicationContext, allergenList)
        allergensRecycleView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun hideKeyboard(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            view.apply {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
