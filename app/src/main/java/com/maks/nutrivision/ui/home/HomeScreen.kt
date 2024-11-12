package com.maks.nutrivision.ui.home
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDownCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.R
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.ui.common.BottomBar
import com.maks.nutrivision.ui.common.CounterButton
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.NutrivisionTheme
import com.maks.nutrivision.ui.theme.Primary
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavHostController,
               cat_id: String? = null,
               viewModel: ProductViewModel = hiltViewModel()) {

    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state
    LaunchedEffect (key1 = true){
       viewModel.getProductsByCategory(cat_id ?: "")
   }
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
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
bottomBar = { BottomBar(navController = navController) }
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }else{
            MainContent(state.products, addToCart =
            {product: Product, i: Int ->
            viewModel.addToCart( product,i)
                coroutineScope.launch {
                    // using the `coroutineScope` to `launch` showing the snackbar
                    // taking the `snackbarHostState` from the attached `scaffoldState`
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Item added to cart",
                        actionLabel = "ok"
                    )
                }
            }, removeFromCart = {
                if(it.getSelectedCount()>0) {
                    viewModel.removeFromCart(it)
                }
            })
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
        }
        }
    }
}


@Composable
fun MainContent(
    productList: List<Product>,
    addToCart: (product: Product, selectedIndex: Int) -> Unit = { product: Product, i: Int -> },
    removeFromCart: (product: Product) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 1),)
        {
            items(productList) { product ->
                PlantCard(product, removeFromCart = {
                    removeFromCart(product)
                },
                    addToCart = {product: Product, i: Int -> addToCart(product,i) })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(product: Product, removeFromCart: (product: Product) -> Unit = {},
              addToCart: (product: Product, selectedIndex: Int) -> Unit = { product: Product, i: Int -> }) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val gmsWeights = listOf(product.opt1_name, product.opt2_name, product.opt3_name)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = {
        }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
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


            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = product.product_name,
                    style = MaterialTheme.typography.body1.plus(TextStyle(fontWeight = FontWeight.Bold)),
                    color = DarkTextColor,
                )
                Row(modifier = Modifier.padding(start = 8.dp). fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(modifier = Modifier.padding(top = 10.dp),
                        text = "₹ ${getPriceByIndex(product, itemPosition.value)}",
                        style = MaterialTheme.typography.body1,
                        color = DarkTextColor,
                    )
                    Box(modifier = Modifier.wrapContentWidth()
                        .wrapContentHeight()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            Primary,
                            shape = RoundedCornerShape(8.dp),
                        )) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                isDropDownExpanded.value = true
                            }
                        ) {
                            Text(modifier = Modifier.padding(6.dp), text = gmsWeights[itemPosition.value])
                            androidx.compose.material3.IconButton(onClick = {isDropDownExpanded.value = true}) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowDropDownCircle,
                                    contentDescription = "",
                                    tint = Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = isDropDownExpanded.value,
                            onDismissRequest = {
                                isDropDownExpanded.value = false
                            }) {
                            gmsWeights.forEachIndexed { index, gmsWeight ->
                                DropdownMenuItem(text = {
                                    Text(text = gmsWeight)
                                },
                                    onClick = {
                                        isDropDownExpanded.value = false
                                        itemPosition.value = index
                                        product.selectedIndex = index

                                    })
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(4.dp))

                CounterButton(onValueDecreaseClick = { removeFromCart(product) },
                    onValueIncreaseClick = { addToCart(product, itemPosition.value) },
                    onValueClearClick = { /*TODO*/ },
                    value = "${getCount(product,itemPosition.value)}",
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .align(Alignment.End)
                )
            }

        }
    }
}

fun getCount(product: Product,itemPosition:Int): Int
{
    return when(itemPosition){
            1 -> product.opt2_count
            2 -> product.opt3_count
            else -> product.opt1_count
        }
}

fun getPriceByIndex(product: Product, value: Int): String {
    when(value) {
        0 -> return product.opt1_price
        1 -> return product.opt2_price;
        2 -> return product.opt3_price
        else -> return product.opt1_price
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutrivisionTheme {
MainContent(productList = listOf(Product(
    "1",
    "Product 1",
    "100g",
    "100.0",
    "img_url",
    "1",
    "100.0",
    offer_id = "offer_id",
    product_name = "Cabbage",
    short_desc = "short_desc",
    size = "size",
    weight = "220g",
    opt1_name = "250 gm",
    opt1_qty = "250",
    opt1_price = "100.0",
    opt2_name = "500 gm",
    opt2_qty = "500",
    opt2_price = "200.0",
    opt3_name = "1 Kg",
    opt3_qty = "1000",
    opt3_price = "300.0",
    selectedIndex = 0
)),
        addToCart = {product: Product, i: Int -> }, removeFromCart = {})
    }
}