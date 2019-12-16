package com.example.veganapp.data

import com.example.veganapp.model.Dish
import com.example.veganapp.model.Restaurant

fun getRestaurantMocks(): ArrayList<Restaurant> {
    val restaurants = ArrayList<Restaurant>()

    restaurants.add(
        Restaurant(
            "Restauracja Ratuszova",
            "Stary Rynek 55",
            "Wegetariańska",
            "ratuszova.jpg",
            listOf<Dish>(
                Dish("asdfdg", listOf<String>("mleko", "orzechy"))
            )
        )
    )

    restaurants.add(
        Restaurant(
            "Matii Sushi",
            "pl. Wladyslawa Andersa 5",
            "Azjatycka",
            "matii_sushi.jpg",
            listOf<Dish>(
                Dish("asdfdg", listOf<String>("mleko", "orzechy")),
                Dish("qwerty", listOf<String>("coś", "innego"))
            )
        )
    )

    return restaurants
}