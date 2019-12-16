package com.example.veganapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.veganapp.model.Diet
import kotlinx.android.synthetic.main.diet_recycler_row.view.*
import org.jetbrains.anko.toast


class DietListRecyclerViewAdapter(
    private val context: DietList,
    private val dietList: ArrayList<Diet>
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


        holder.itemView.setOnClickListener {
            context.toast("TODO")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dietName: TextView? = view.dietNameTextView
    }
}