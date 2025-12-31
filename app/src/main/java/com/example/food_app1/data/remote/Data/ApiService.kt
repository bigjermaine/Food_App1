package com.example.food_app1.data.remote.Data

import com.example.food_app1.Presentation.Models.MealData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ApiService {
    private val api: MealsAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(MealsAPI::class.java)
    }

    suspend fun getMealsByCategory(category: String): MealData {
        return api.getMealsByCategory(category)
    }

    // 🔹 Search meals by name (FULL details)
    suspend fun searchMeals(query: String): MealData {
        return api.searchMeals(query)
    }
    interface MealsAPI {
        @GET("filter.php")
        suspend fun getMealsByCategory(
            @Query("c") category: String
        ): MealData

        @GET("search.php")
        suspend fun searchMeals(
            @Query("s") query: String
        ): MealData
    }
}