package com.example.food_app1.Presentation.Views.Home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.food_app1.Presentation.ViewModels.FavoritesViewModel
import com.example.food_app1.Extension.ingredientsUpTo10
import com.example.food_app1.Presentation.ViewModels.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    mealId: String,
    viewModel: HomeViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
    onBack: () -> Unit
) {
    val isFavorite by favoritesViewModel
        .isFavorite(mealId)
        .collectAsState()
    val meal = viewModel.meals.collectAsState().value
        .firstOrNull { it.idMeal == mealId }
    val ingredients = meal?.ingredientsUpTo10().orEmpty()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->

            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 🖼️ BIG HEADER IMAGE
                item {
                    AsyncImage(
                        model = meal?.strMealThumb,
                        contentDescription = meal?.strMeal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Text(
                        text = meal?.strMeal ?: "Meal Details",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
                item {
                    Text(
                        text = "Meal ID: $mealId",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Text(
                        text = "Meal Ingredients",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


                items(ingredients) { ingredient ->
                    Text(
                        text = "• $ingredient",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    meal?.strIngredient1?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                item {
                    meal?.strIngredient2?.let {
                        Text(
                            text =it,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                item {
                    IconButton(
                        onClick = {
                            favoritesViewModel.toggleFavorite(
                                meal = meal!!,
                                isFavorite = isFavorite
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
            }


        }


        }


    }


