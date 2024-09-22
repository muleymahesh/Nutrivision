package com.maks.nutrivision.ui.home
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.R
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.ui.common.BottomBar
import com.maks.nutrivision.ui.common.bottomNavItems
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.Primary
import kotlinx.coroutines.CoroutineScope
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
       viewModel.getProducts(cat_id)
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

            MainContent(context,navController, state.products
            ) {
                viewModel.addToCart(it)
                coroutineScope.launch {
                    // using the `coroutineScope` to `launch` showing the snackbar
                    // taking the `snackbarHostState` from the attached `scaffoldState`
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Item added to cart",
                        actionLabel = "Close"
                    )
                }
            }
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
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
fun MainContent(
    context: Context,
    navController: NavController,
    productList: List<Product>,
    addToCart: (product: Product) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp))
        {
            items(productList) { product ->
                PlantCard(product, onClick = {
                    //navController.navigate("detail_screen?product=${Gson().toJson(it)}")
                },
                    addToCart = {addToCart(product) })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(product: Product, onClick: (product: Product) -> Unit = {},
              addToCart: (product: Product) -> Unit = {}) {
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
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .align(Alignment.CenterHorizontally),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://freshfoodretailers.com/admin/product_image/${product.img_url}")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = product.product_name,
                contentScale = ContentScale.Fit,

                )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = product.product_name,
                style = MaterialTheme.typography.h6,
                color = DarkTextColor,
            )

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {

                Column(
                    Modifier
                        .padding(8.dp)
                        .weight(1f)) {
                    Text(
                        text = product.weight,
                        color = Color.Gray,
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹ ${product.mrp}",
                        style = MaterialTheme.typography.h6,
                        color = DarkTextColor,
                    )
                }
                Button(
                    onClick = {addToCart(product)},
                    shape = RoundedCornerShape(23.dp),
                    border = BorderStroke(2.dp, DarkTextColor),
                    colors = ButtonDefaults.buttonColors( backgroundColor = DarkTextColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}