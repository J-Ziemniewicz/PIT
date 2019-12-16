package com.example.veganapp.model

data class Restaurant(
    var name: String,
    var address: String,
    var type: String,
    var imagePath: String,
    var menu: List<Dish>
)

data class Dish(
    var description: String,
    var allergens: List<String>
)
