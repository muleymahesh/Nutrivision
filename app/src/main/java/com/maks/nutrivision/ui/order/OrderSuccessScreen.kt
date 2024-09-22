package com.maks.nutrivision.ui.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.maks.nutrivision.R
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.Primary


@Composable
fun OrderSuccessScreen(navController: NavHostController 
) {
   
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp
            ) {
                IconButton(onClick = { navController.popBackStack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = Color.White)                     }
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        bottomBar = {
            Column {
              OutlinedButton(
                    onClick = { 
                        navController.popBackStack()
                              navController.popBackStack()},
                  Modifier
                      .fillMaxWidth()
                      .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Primary)
                ) {
                    Text(modifier = Modifier.padding(8.dp),
                        text = "Okay",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            } }
    ) { contentPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    color = AppBg
                )
                .padding(contentPadding)
        ) {

              NormalTextComponent(value = "Your order placed Successfully !!!")  
        }
    }
}
