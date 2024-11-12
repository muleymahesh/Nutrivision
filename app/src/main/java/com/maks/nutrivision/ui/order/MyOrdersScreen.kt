package com.maks.nutrivision.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maks.nutrivision.R
import com.maks.nutrivision.data.entities.Order
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.Primary
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun MyOrdersScreen(navController: NavHostController ,u_id: String,
                   placeOrderViewModel: PlaceOrderViewModel = hiltViewModel()
) {
    val state = placeOrderViewModel.orderState
    LaunchedEffect(key1 = true) {
        placeOrderViewModel.getMyOrders(u_id)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = Color.White)
                }
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
    ) { contentPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = AppBg
                    )
                    .padding(contentPadding)
            ) {

                if (state.isLoading) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))

                        CircularProgressIndicator(modifier = Modifier.weight(1f))
                    }
                } else{

                    LazyColumn( modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(16.dp)
                    ) {
                        if (state.orderList.isNotEmpty()) {
                            item { NormalTextComponent(value = "My Orders") }
                            items(items = state.orderList) {
                                Spacer(modifier = Modifier.height(4.dp))
                                OrderCard(order = it)
                            }
                        }else{
                            item { NormalTextComponent(value = "No Orders Found") }
                        }
                    }
                }
            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderCard(order :Order){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = {}
    ) {
        Column(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Info, // Replace with your arrow resource
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp),
                    tint = Primary
                )
                Column(modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)) {
                    Text(
                        text = "Order ID: ${order.o_id} - (${order.details.size} items) ",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ordered On: ${order.order_date} ",
                        style = TextStyle(fontSize = 16.sp)
                    )

                }
            }
            HorizontalDivider(thickness = 2.dp, modifier = Modifier
                .fillMaxWidth(), color = Primary)
            Spacer(modifier = Modifier
                .height(8.dp))
            Column(modifier = Modifier
                .padding(8.dp)
                ) {
                for (detail in order.details) {
                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${detail.product_name} x ${detail.qty}",
                            style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                        )
                        Text(
                            text = "Rs ${detail.price}",
                            style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                        )
                    }
                }
                Spacer(modifier = Modifier
                    .height(8.dp))
                Row {
                    Text(modifier = Modifier.weight(1f),
                        text = "Amount:",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                    )
                    Text(
                        text = "Rs ${order.total}",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                    )
                }
                Spacer(modifier = Modifier
                    .height(8.dp))
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Status: ",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                    )
                    Text (
                            text = "${order.order_status} ",
                    style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                    )
                }
            }
        }
    }
}