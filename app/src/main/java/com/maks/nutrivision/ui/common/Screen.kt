package com.maks.nutrivision.ui.common

// Screen.kt
sealed class Screen(val route: String) {
    object Splash: Screen("splash_screen")
    object Main: Screen("main_screen")
    object Detail: Screen("detail_screen")
    object Dashboard: Screen("dashboard_screen")
    object Cart: Screen("cart_screen")
    object Login: Screen("login_screen")
    object Register: Screen("register_screen")
    object Profile: Screen("profile_screen")
}