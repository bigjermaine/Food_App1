package com.example.food_app1.Extension

import com.example.food_app1.Presentation.Models.MealDetail


fun MealDetail.ingredientsUpTo10(): List<String> =
    listOfNotNull(
        strIngredient1,
        strIngredient2,
        strIngredient3,
        strIngredient4,
        strIngredient5,
        strIngredient6,
        strIngredient7,
        strIngredient8,
        strIngredient9,
        strIngredient10
    ).filter { it.isNotBlank() }
