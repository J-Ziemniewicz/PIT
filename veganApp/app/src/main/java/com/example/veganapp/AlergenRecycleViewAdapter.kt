package com.example.veganapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.veganapp.model.Allergen
import kotlinx.android.synthetic.main.alergen_recycler_row.view.*

class AlergenRecycleViewAdapter(
    private val context: Context,
    private val allergenList: ArrayList<Allergen>
) : RecyclerView.Adapter<AlergenRecycleViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.alergen_recycler_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return allergenList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allergen = allergenList[position]
        holder.allergenName?.text = allergen.name
        holder.allergenState?.isChecked = allergen.state
        holder.itemView.setOnClickListener {
            allergen.state = !allergen.state
            holder.allergenState?.isChecked = allergen.state
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val allergenName: TextView? = view.allergenNameTextView
        val allergenState: CheckBox? = view.allergenStateCheckBox
    }


}