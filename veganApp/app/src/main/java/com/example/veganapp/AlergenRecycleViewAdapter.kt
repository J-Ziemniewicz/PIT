package com.example.veganapp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.veganapp.model.Allergen
import kotlinx.android.synthetic.main.alergen_recycler_row.view.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

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
            Log.d("XDXDXD", allergen.name + " " + allergen.state)
        }
//        holder.allergenState?.onCheckedChange { buttonView, isChecked ->
//            allergen.state = isChecked
//            Log.d("XDXDXD", allergen.name + " " + allergen.state)
//        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val allergenName: TextView? = view.allergenNameTextView
        val allergenState: CheckBox? = view.allergenStateCheckBox
    }


}