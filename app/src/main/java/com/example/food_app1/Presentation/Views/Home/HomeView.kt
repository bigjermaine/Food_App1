package com.example.food_app1.Presentation.Views.Home

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import com.example.food_app1.Presentation.ViewModels.HomeViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.food_app1.Presentation.ViewModels.HomeUiState
import com.example.food_app1.Presentation.Models.MealDetail
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel(), onMealClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Meals Available",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (uiState) {
            is HomeUiState.Loading -> {
                LoadingGridSkeleton()
            }

            is HomeUiState.Success -> {
                val meals = (uiState as HomeUiState.Success).meals

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(meals) { item ->
                        LastWatchedCard(item){ it
                           onMealClick(it)
                        }
                    }
                }
            }

            is HomeUiState.Error -> {
                Text(
                    text = "Something went wrong",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun LastWatchedCard(item: MealDetail,  onClick: (String) -> Unit) {

    Card(
        modifier = Modifier
            .clickable { onClick(item.idMeal) }
            .fillMaxWidth()
            .aspectRatio(1f),
             elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔥 IMAGE
            AsyncImage(
                model = item.strMealThumb,
                contentDescription = item.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.strMeal,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@Composable
fun LoadingGridSkeleton() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(15) {
            SkeletonCard()
        }
    }
}

@Composable
fun SkeletonCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmer()
        )
    }
}
