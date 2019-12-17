package com.example.veganapp

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment.getExternalStorageDirectory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.veganapp.model.Restaurant
import kotlinx.android.synthetic.main.recyclerview_row.view.*
import org.jetbrains.anko.toast
import java.io.File


class RecyclerViewAdapter(
    private val context: Context,
    private val restaurantList: ArrayList<Restaurant>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant: Restaurant = restaurantList[position]

        holder.restaurantName?.text = restaurant.name
        holder.restaurantAddress?.text = restaurant.address
        holder.restaurantType?.text = restaurant.type

        val dir = "Pictures/"
        val pathName =
            getExternalStorageDirectory().toString() + File.separator + dir + restaurant.imagePath
        Log.d("XDXDXD", pathName)

        if (File(pathName).exists()) {
            val myBitmap = BitmapFactory.decodeFile(pathName)
            if (myBitmap != null) {
                holder.restaurantThumbnail?.setImageBitmap(myBitmap)
            }
        }

//        val scaledBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, false)


        holder.itemView.setOnClickListener {
            context.toast("TODO")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView? = view.restaurantNameTextView
        val restaurantAddress: TextView? = view.restaurantAddressTextView
        val restaurantType: TextView? = view.restaurantTypeTextView
        val restaurantThumbnail: ImageView? = view.restaurantThumbnailImageView
    }
}