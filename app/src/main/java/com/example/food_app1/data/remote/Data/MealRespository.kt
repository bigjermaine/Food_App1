package com.example.food_app1.data.remote.Data

import com.example.food_app1.Presentation.Models.MealData

class MealRespository(private val apiService: ApiService) {

    suspend fun getMeals(): MealData {
        val meal =  apiService.getMealsByCategory("Dessert")
        return meal
    }

    suspend fun searchMeal(category: String): MealData {
        val meal =  apiService.searchMeals(category )
        return meal
    }

    companion object {
        @Volatile
        private var instance: MealRespository? = null

        fun getInstance(webService: ApiService = ApiService()): MealRespository {
            return instance ?: synchronized(this) {
                instance ?: MealRespository(webService).also { instance = it }
            }
        }
    }
}