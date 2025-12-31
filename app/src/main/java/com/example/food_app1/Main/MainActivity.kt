package com.example.food_app1.Main
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food_app1.data.remote.Data.MealRespository
import com.example.food_app1.data.local.Data.DatabaseProvider
import com.example.food_app1.data.local.Respository.FavoritesRepository
import com.example.food_app1.Presentation.ViewModels.FavoritesViewModel
import com.example.food_app1.Presentation.Components.TwitterLikeBottomBar
import com.example.food_app1.Presentation.ViewModels.HomeViewModel
import com.example.food_app1.Presentation.Views.Home.HomeView
import com.example.food_app1.Presentation.Views.Home.MealDetailsScreen
import com.example.food_app1.Presentation.Views.Favourite.FavoritesScreen
import com.example.food_app1.Presentation.Views.Search.SearchMealsScreen
import com.example.food_app1.ui.theme.Food_App1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Food_App1Theme {
                TwitterLikeTabBarApp()
            }
        }
    }
}

@Preview()
@Composable
fun test(){
    TwitterLikeTabBarApp()
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun TwitterLikeTabBarApp() {
    val homeViewModel
      =  HomeViewModel(MealRespository.getInstance())
    val context = LocalContext.current
    val favoritesRepository = remember {
        FavoritesRepository(
            DatabaseProvider.getDatabase(context).favoriteDao()
        )
    }
    val favoritesViewModel
            = FavoritesViewModel(repository = favoritesRepository)
    val navController = rememberNavController()
    val tabs = remember {
        listOf(
            TabItem.Home,
            TabItem.Search,
            TabItem.Spaces,
        )
    }
    Scaffold(
        bottomBar = {
            TwitterLikeBottomBar(
                tabs = tabs,
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TabItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TabItem.Home.route) {
                HomeView(viewModel = homeViewModel) {  mealId ->
                    navController.navigate(
                        Screen.MealDetails.createRoute(mealId)
                    )
                }
            }
            composable(TabItem.Search.route) {
                FavoritesScreen(viewModel = favoritesViewModel)

            }

            composable(TabItem.Spaces.route) {
                SearchMealsScreen(viewModel = homeViewModel ){  meal ->
                    navController.navigate(
                        Screen.MealDetails.createRoute(meal.idMeal)
                    )
                }
            }


            //Detail View
            composable(
                route = Screen.MealDetails.route,
                arguments = listOf(
                    navArgument("mealId") { type =  NavType.StringType }
                )
            ) { backStackEntry ->
                val mealId =
                    backStackEntry.arguments?.getString("mealId") ?: return@composable
                     MealDetailsScreen(mealId = mealId, viewModel = homeViewModel, favoritesViewModel = favoritesViewModel) {
                         navController.popBackStack()
                     }
            }
        }
    }

    }

    @Composable
    private fun PlaceholderScreen(title: String) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )

    }