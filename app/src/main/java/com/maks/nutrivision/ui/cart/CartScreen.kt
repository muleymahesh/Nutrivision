package com.maks.nutrivision.ui.cart

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
import androidx.compose.material3.HorizontalDivider
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
import com.maks.nutrivision.ui.common.CounterButton
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.Primary


@Composable
fun CartScreen(navController: NavHostController ,
               viewModel: CartViewModel = hiltViewModel(),
               productViewModel: ProductViewModel = hiltViewModel()
) {
    val state = viewModel.state

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
                val total = state.products?.sumOf { it.getPrice().toInt()*(it.getSelectedCount()) }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),)
                NormalTextComponent(value = "Total : ${total?:0}")
                OutlinedButton(
                    onClick = {
                        if(state.products?.isNotEmpty() == true)  navController.navigate("${Screen.PlceOrder.route}?deliverycharges=${state.deliveryCharges}&handlingcharges=${state.handlingCharges}") },
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Primary)
                ) {
                    Text(modifier = Modifier.padding(8.dp),
                        text = "Checkout",
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

            if (state.isLoading) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    NormalTextComponent(value = "My Cart")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else{
                MainContent(navController, state.products
                , removeFromCart = { productViewModel.removeFromCart(it) },
                   addToCart =  { product: Product, i: Int ->  productViewModel.addToCart(product,i) }
                )
            }
        }
    }
}


@Composable
fun MainContent(
    navController: NavController,
    productList: List<Product>,
    removeFromCart: (product: Product) -> Unit = {},
    addToCart: (Product,Int) -> Unit = {product: Product, i: Int -> }
) {
    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
    ) {
            item { NormalTextComponent(value = "My Cart") }
            items(productList) { product ->
                PlantCard(product, onClick = {
                    navController.navigate("detail_screen?product=${Gson().toJson(it)}")
                }, removeFromCart = removeFromCart,
                addToCart = { product: Product, i: Int ->  addToCart(product,i)})
            }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(
    product: Product, onClick: (product: Product) -> Unit = {},
    removeFromCart: (product: Product) -> Unit = {},
    addToCart: (Product, Int) -> Unit = { product: Product, i: Int -> }) {
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
                    style = MaterialTheme.typography.h6,
                    color = DarkTextColor,
                )
                Column(
                    Modifier
                        .padding(8.dp)) {
                    Text(
                        text = product.getNamedWeight(),
                        color = Color.Gray,
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹ ${product.getPrice().toInt()*product.getSelectedCount()}",
                        style = MaterialTheme.typography.h6,
                        color = DarkTextColor,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 8.dp)) {
                Spacer(modifier = Modifier.height(10.dp).padding(end = 16.dp))

                CounterButton(onValueDecreaseClick = { removeFromCart(product) },
                    onValueIncreaseClick = { addToCart(product,product.selectedIndex) },
                    onValueClearClick = { /*TODO*/ },
                    value = "${product.getSelectedCount()}",
                    modifier = Modifier
                        .width(140.dp)
                        .height(40.dp)
                )
                Spacer(modifier = Modifier.height(30.dp).padding(end = 16.dp))
                Text(
                    text = "Qty ${product.getSelectedCount()}",
                    style = MaterialTheme.typography.h6,
                    color = DarkTextColor,
                )
            }
        }
    }
}
