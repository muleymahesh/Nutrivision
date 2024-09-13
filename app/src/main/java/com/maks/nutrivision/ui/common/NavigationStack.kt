package com.maks.nutrivision.ui.common
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maks.nutrivision.ui.SplashScreen
import com.maks.nutrivision.ui.cart.CartScreen
import com.maks.nutrivision.ui.dashboard.DashboardScreen
import com.maks.nutrivision.ui.detail.DetailScreen
import com.maks.nutrivision.ui.home.HomeScreen
import com.maks.nutrivision.ui.user.LoginScreen
import com.maks.nutrivision.ui.user.ProfileScreen
import com.maks.nutrivision.ui.user.SignupScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Main.route + "?cat={text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                    nullable = true
                }
            )) {
            HomeScreen(navController = navController, cat_id = it.arguments?.getString("text"))
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route + "?product={text}",
            arguments = listOf(
                navArgument("product") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            DetailScreen(text = it.arguments?.getString("product"))
        }
        composable(route = Screen.Cart.route) {
            CartScreen(navController = navController)
        }

        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            SignupScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}





