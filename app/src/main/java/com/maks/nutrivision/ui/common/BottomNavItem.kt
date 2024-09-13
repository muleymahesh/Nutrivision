package com.maks.nutrivision.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Home",
        route = Screen.Dashboard.route,
        icon = Icons.Rounded.Home,

    ),

    BottomNavItem(
        name = "Cart",
        route = Screen.Cart.route,
        icon = Icons.Rounded.ShoppingCart,
    ),
    BottomNavItem(
        name = "Profile",
        route = Screen.Profile.route,
        icon = Icons.Rounded.Person,
    ),
    )