package com.example.veganapp.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

class Diet(var name: String) : Parcelable {
    val allergens = ArrayList<Allergen>()

    constructor(parcel: Parcel) : this(parcel.readString()) {
        val lista = arrayListOf<Allergen>().apply {
            parcel.readList(this, Allergen::class.java.classLoader)
        }
        Log.d("XDXDXD", lista.toString())
        allergens.addAll(lista)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeList(allergens)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Diet> {
        override fun createFromParcel(parcel: Parcel): Diet {
            return Diet(parcel)
        }

        override fun newArray(size: Int): Array<Diet?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class Allergen(var name: String, var state: Boolean) : Parcelable
