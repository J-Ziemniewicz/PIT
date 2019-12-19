package com.example.veganapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veganapp.model.Diet
import kotlinx.android.synthetic.main.activity_edit_diet.*
import kotlinx.android.synthetic.main.activity_new_diet.*
import kotlinx.android.synthetic.main.activity_new_diet.dietNameEditText
import kotlinx.android.synthetic.main.activity_new_diet.saveDietButton
import kotlinx.android.synthetic.main.allergen_choice.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class EditDiet : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_diet)

        val diet = intent.getParcelableExtra("diet") as Diet
        val allergenList = diet.allergens

        dietNameEditText.setOnFocusChangeListener { view, b -> hideKeyboard(view, b) }
        dietNameEditText.setText(diet.name)

        layoutManager = LinearLayoutManager(this)
        allergensRecycleView.layoutManager = layoutManager
        allergensRecycleView.adapter = AlergenRecycleViewAdapter(this.applicationContext, allergenList)
        allergensRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        saveDietButton.setOnClickListener {
            DbHelper(applicationContext).editDiet(
                getCurrentUser(applicationContext),
                dietNameEditText.text.toString(),
                allergenList
            )
            toast("Zapisano")
            finish()
        }

        cancelButton.setOnClickListener { onBackPressed() }
    }

//    override fun onBackPressed() {
//        alert("Zmiany nie zostaną zapisane") {
//            title = "Powrót"
//            yesButton {
//                super.onBackPressed()
//            }
//            noButton {}
//        }.show()
//    }

    private fun hideKeyboard(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            view.apply {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}