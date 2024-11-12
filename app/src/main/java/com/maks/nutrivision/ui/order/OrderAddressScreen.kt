package com.maks.nutrivision.ui.order

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.Primary


val shippingType: String = "CASH ON DELIVERY"
var total: Double = 0.0

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OrderAddressScreen(navController: NavHostController,
                       deliveryCharges: Int,handlingCharges: Int,
                       viewModel: PlaceOrderViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp
            ) {
                IconButton(onClick = { navController.popBackStack()
                    navController.popBackStack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = Color.White)                     }
                Text(
                    text = "Checkout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        bottomBar = { OutlinedButton(onClick = { viewModel.placeOrder(navController, total, shippingType) },
            Modifier
                .fillMaxWidth()
                .padding(8.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Primary)) {
            Text(modifier = Modifier.padding(8.dp), text = "Place Order" , color = Color.White, style = TextStyle(fontSize = 20.sp))
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

            LazyColumn(modifier = Modifier
                .fillMaxSize(1f)
                .padding(16.dp)) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 3.dp,
                        backgroundColor = AppBg

                    ) {
                        Column(modifier = Modifier.padding(start = 16.dp),
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Delivering to:",
                                style = MaterialTheme.typography.body1,
                                color = DarkTextColor,
                            )

                        Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = state.name,
                                style = MaterialTheme.typography.h6,
                                color = Color.Black,
                            )
                            Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = state.address,
                            style = MaterialTheme.typography.body1,
                            color = Color.Black,
                        )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Contact. ${state.mobile}",
                                style = MaterialTheme.typography.body1,
                                color = Color.Gray,
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                    }
                }
                item { MainContent(navController = navController, productList = state.productList) }

                item { Spacer(
                    modifier = Modifier.height(36.dp),
                ) }

                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 3.dp,
                        backgroundColor = AppBg

                    ) {
                        Column(modifier = Modifier.padding(start = 16.dp),
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            total = getCartTotal(state.productList) + handlingCharges

                                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 8.dp)) {
                                    Text(text = "Delivery charges :₹ ${if(total<400) deliveryCharges else 0.0}",style = MaterialTheme.typography.body1, color = Color.Gray, )
                                    Text(text = "Handling charges :₹ $handlingCharges",style = MaterialTheme.typography.body1, color = Color.Gray,)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if(total<400) {
                                        total += deliveryCharges
                                        Text(
                                            text = "Total amount :₹ ${total}",
                                            style = MaterialTheme.typography.body1.plus(
                                                TextStyle(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ),
                                        )
                                    }else{
                                        Text(text = "Total amount :₹ ${total}",
                                            style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                                        )
                                    }
                                }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Payment Mothod:",
                                style = MaterialTheme.typography.body1,
                                color = DarkTextColor,
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "CASH ON DELIVERY",
                                style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                                color = Color.Black,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                        }
                    }
                }

            }
        }
    }
}

fun getCartTotal(productList: List<Product>): Double {
    var total: Double = 0.0
    productList.forEach {
        total += it.getPrice().toInt() * it.getSelectedCount().toInt()
    }
    return total
}

@Composable
fun MainContent(
    navController: NavController,
    productList: List<Product>,
) {
    Column{
        productList.forEach { movie ->
            PlantCard(movie, onClick = {
                //navController.navigate("detail_screen?product=${Gson().toJson(it)}")
            })
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(product: Product, onClick: (product: Product) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = {
            onClick(product)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .align(Alignment.CenterVertically),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://freshfoodretailers.com/admin/product_image/${product.img_url}")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = product.product_name,
                contentScale = ContentScale.Fit,

                )

            Column(Modifier.weight(1f),
                verticalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = product.product_name,
                    style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                    color = DarkTextColor,
                )
                Column(
                    Modifier
                        .padding(8.dp)) {
                    Text(
                        text = product.getNamedWeight(),
                        color = Color.Gray,
                        style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                        )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹ ${product.getPrice()}",
                        style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                        color = DarkTextColor,
                    )
                }
            }
            Column {

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Qty ${product.getSelectedCount().toInt()}",
                    style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                    color = DarkTextColor,
                )
            }
        }
    }
}
