package com.example.veganapp

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.View
import com.example.veganapp.model.Restaurant
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.layout_custom_map_marker.view.*
import java.io.File

class MapInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.layout_custom_map_marker, null)
        var mInfoWindow: Restaurant? = p0?.tag as Restaurant?

        mInfoView.name.text = mInfoWindow?.name
        mInfoView.address.text = mInfoWindow?.address
        mInfoView.hours.text = mInfoWindow?.hours
        mInfoView.restaurant_type.text = mInfoWindow?.type


        val dir = "Pictures/"
        val pathName =
            Environment.getExternalStorageDirectory().toString() + File.separator + dir + mInfoWindow?.imagePath
        Log.d("XDXDXD", pathName)

        if (File(pathName).exists()) {
            val myBitmap = BitmapFactory.decodeFile(pathName)
            if (myBitmap != null) {
                mInfoView.picture?.setImageBitmap(myBitmap)
            }
        }

        return mInfoView
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }


}