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
    object EditProfile: Screen("edit_profile_screen")
    object PlceOrder: Screen("place_order_screen")
    object OrderSuccess: Screen("order_success_screen")
    object MyOrders: Screen("my_orders_screen")
    object ForgotPassword: Screen("forgot_password_screen")
}