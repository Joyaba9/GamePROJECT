package com.example.gameproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gameproject.ui.home.HomePageView
import com.example.gameproject.ui.login.LoginView
import com.example.gameproject.ui.profile.ProfileView
import com.example.gameproject.ui.register.RegisterView
import com.example.gameproject.ui.splash.SplashView
import edu.farmingdale.draganddropanim_demo.DragAndDropBoxes


@Composable
fun gameprojectNavigation(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    barIsVisible: (Boolean) -> Unit = { input -> input }

) {
    NavHost(
        navController = navController,
        startDestination = gameprojectDestinations.Splash.route,
        modifier = modifier
    ) {
        composable(gameprojectDestinations.Home.route) {
            barIsVisible(true)
            HomePageView(navController)
        }
        composable(gameprojectDestinations.Login.route) {
            barIsVisible(false)
            LoginView(navController, modifier)
        }
        composable(gameprojectDestinations.Register.route) {
            barIsVisible(false)
            RegisterView(navController, modifier)
        }
        composable(gameprojectDestinations.Profile.route) {
            barIsVisible(true)
            ProfileView(navController)
        }

        composable(gameprojectDestinations.Splash.route) {
            barIsVisible(false)
            SplashView(navController)
        }
        composable(gameprojectDestinations.EasyGame.route){
            barIsVisible(true)
            DragAndDropBoxes()
        }
    }

    fun composable(route: Any, function: () -> Unit) {

    }

    // Extension function for NavHostController to navigate to a destination.
    fun NavHostController.navigateToDestination(destination: gameprojectDestinations) {
        this.navigate(destination.route) {
            launchSingleTop = true
            restoreState = true
        }
    }
}