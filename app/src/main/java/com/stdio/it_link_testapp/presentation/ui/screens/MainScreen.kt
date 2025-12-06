package com.stdio.it_link_testapp.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stdio.it_link_testapp.presentation.ui.screens.imageDetail.ImageDetailScreen
import com.stdio.it_link_testapp.presentation.ui.screens.images.ImagesScreen

@Composable
fun MainScreen(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "images"
    ) {
        composable("images") {
            ImagesScreen(modifier) { index ->
                navController.navigate("detail/$index")
            }
        }

        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            ImageDetailScreen(modifier, index)
        }
    }
}