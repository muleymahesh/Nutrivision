package com.maks.nutrivision.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.gson.Gson
import com.maks.nutrivision.R
import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.DarkTextColor
import com.maks.nutrivision.ui.theme.Primary
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DashboardScreen(navController: NavHostController,
                    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getBanner()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp
            ) {
                Spacer(modifier = Modifier.width(22.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
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

            val banners = viewModel.banner.observeAsState()
            val categories = viewModel.category.observeAsState()
            MainContent(navController, banners.value ?: listOf(),categories.value ?: listOf())

            if (banners.value.isNullOrEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(
    navController: NavController,
    bannerList: List<Banner>,
    categoryList: List<Category>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.height(200.dp)) {
            Card(
                modifier = Modifier.padding(6.dp),
                shape = RoundedCornerShape(6.dp),
            ) {
                if (bannerList.isNotEmpty())
                    AutoSlidingCarousel(
                        itemsCount = bannerList.size,
                        itemContent = { index ->
                            AsyncImage(
                                model = "https://freshfoodretailers.com/admin/product_image/${bannerList[index].image_path}",
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.height(200.dp)
                            )
                        }
                    )
            }
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Categories",
            style = MaterialTheme.typography.h5,
            color = DarkTextColor,
        )
        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = 1000.dp),
            columns = GridCells.Adaptive(128.dp),

            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(categoryList) { cat ->
                    Box(modifier = Modifier.padding(6.dp)) {
                        PlantCard(cat.cat_name, cat.cat_img, onClick = {
                            navController.navigate("${Screen.Main.route}?cat=${cat.cat_id}}")
                        })
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(name: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = onClick
    ) {
Box(contentAlignment = Alignment.BottomCenter) {
    AsyncImage(
        modifier = Modifier.fillMaxSize(),
        model = "https://freshfoodretailers.com/admin/product_image/$description",
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
    Row(
        Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center
            ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = name,
            style = MaterialTheme.typography.h6,
            color = DarkTextColor,
        )
    }
}
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Yellow,
    unSelectedColor: Color = Color.Gray ,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 4000,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState.currentPage) {
        delay(autoSlideDuration)
        pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparant bacground
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}