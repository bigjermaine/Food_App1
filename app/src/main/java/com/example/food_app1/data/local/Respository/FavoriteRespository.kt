package com.example.food_app1.data.local.Respository

import com.example.food_app1.data.local.Data.FavoriteMealDao
import com.example.food_app1.data.local.Data.FavoriteMealEntity
import com.example.food_app1.Presentation.Models.MealDetail
class FavoritesRepository(
    private val dao: FavoriteMealDao
) {

    fun favorites() = dao.getFavorites()

    fun isFavorite(mealId: String) = dao.isFavorite(mealId)

    suspend fun addFavorite(meal: MealDetail) {
        val entity = FavoriteMealEntity(
            mealId = meal.idMeal,
            name = meal.strMeal,
            image = meal.strMealThumb
        )
        dao.addFavorite(entity)
    }

    suspend fun removeFavorite(mealId: String) {
        dao.removeFavorite(mealId)
    }

    suspend fun toggleFavorite(meal: MealDetail, isFavorite: Boolean) {
        if (isFavorite) {
            removeFavorite(meal.idMeal)
        } else {
            addFavorite(meal)
        }
    }
}
