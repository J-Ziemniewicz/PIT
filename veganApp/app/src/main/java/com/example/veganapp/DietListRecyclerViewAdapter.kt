package com.example.veganapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.example.veganapp.model.Allergen
import com.example.veganapp.model.Diet
import kotlinx.android.synthetic.main.diet_recycler_row.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity


class DietListRecyclerViewAdapter(
    private val context: DietList,
    private val dietList: ArrayList<Diet>,
    private var lastChecked: Int
) :
    RecyclerView.Adapter<DietListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.diet_recycler_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dietList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diet: Diet = dietList[position]

        holder.dietName?.text = diet.name
        holder.dietAllergens?.text = getActiveAllergens(diet.allergens)


        holder.itemView.setOnClickListener {
            context.startActivity<EditDiet>("diet" to diet)
        }

        holder.radioButton.isChecked = lastChecked == position

        holder.radioButton.onClick {
            lastChecked = position
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dietName: TextView? = view.dietNameTextView
        val dietAllergens: TextView? = view.dietAllergensTextView
        val radioButton: RadioButton = view.radioButton
    }

    private fun getActiveAllergens(allergenList: ArrayList<Allergen>): String {
        var str = "Alergeny: "
        allergenList.forEach {
            if (it.state) {
                str += it.name
                str += ", "
            }
        }
        return str.removeSuffix(", ")
    }
}