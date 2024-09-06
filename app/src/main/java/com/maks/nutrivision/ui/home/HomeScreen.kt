package com.maks.nutrivision.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.ui.common.Screen

@Composable
fun HomeScreen(navController: NavController,
               viewModel: ProductViewModel = hiltViewModel<ProductViewModel>()
) {
    val text = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.getBanner()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
       


    }
}