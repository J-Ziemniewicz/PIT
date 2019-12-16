package com.example.veganapp.model

class Diet(var name: String) {
    val allergens = ArrayList<Allergen>()
}

data class Allergen(var name: String, var state: Boolean)
