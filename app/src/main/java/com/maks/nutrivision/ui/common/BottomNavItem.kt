package com.maks.nutrivision.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
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
        route = "home",
        icon = Icons.Rounded.Home,
    ),
    BottomNavItem(
        name = "Categories",
        route = "category",
        icon = Icons.Rounded.Menu,
    ),
    BottomNavItem(
        name = "Cart",
        route = "cart",
        icon = Icons.Rounded.ShoppingCart,
    ),
)