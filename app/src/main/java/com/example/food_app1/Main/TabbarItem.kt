package com.example.food_app1.Main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector



sealed class TabItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : TabItem("home", Icons.Filled.Home, "Home")
    data object Search : TabItem("Favourite",Icons.Outlined.FavoriteBorder, "Favourite")
    data object Spaces : TabItem("Search", Icons.Filled.Search, "Search")

}
sealed class Screen(val route: String) {
    object MealDetails : Screen("meal_details/{mealId}") {
        fun createRoute(mealId: String) = "meal_details/$mealId"
    }
}
