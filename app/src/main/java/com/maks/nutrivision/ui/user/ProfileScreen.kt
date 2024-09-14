package com.maks.nutrivision.ui.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maks.nutrivision.R
import com.maks.nutrivision.ui.ProductViewModel
import com.maks.nutrivision.ui.common.BottomBar
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.home.MainContent
import com.maks.nutrivision.ui.theme.AppBg
import com.maks.nutrivision.ui.theme.Primary


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(navController: NavHostController,
                  userViewModel: UserViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        userViewModel.getAllUsers()
    }
    val profile = userViewModel.profileList.collectAsState()

    androidx.compose.material.Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp
            ) {
                androidx.compose.material.IconButton(onClick = { navController.popBackStack() }) {
                    androidx.compose.material.Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "",
                        tint = Color.White
                    )
                }
                androidx.compose.material.Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                )
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
                    .background(Primary)
                    .fillMaxWidth()
                ,
                //contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Image
                ProfileImage()

                Spacer(modifier = Modifier.height(16.dp))

                profile.value.firstOrNull()?.let { NormalTextComponent(value = "Welcome ${it.fname}", textColor = Color.White) }
            }
            LazyColumn( ){
                item {
                    ListItemWithImage(Icons.Rounded.AccountBox, title = "Edit Profile") {

                    }
                    
                }
            }
        }
    }
}




@Composable
fun ProfileImage() {
    val imagePainter = rememberAsyncImagePainter(
        model = "https://www.shutterstock.com/image-vector/vector-flat-illustration-grayscale-avatar-600nw-2281862025.jpg", // Replace with a real URL or a local image
    )

    Box(contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = imagePainter,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
        )

    }
}


@Composable
fun ListItemWithImage(
    imageVector: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Icon/Image at the start of the item
        Icon(
            imageVector = imageVector, // Replace with your arrow resource
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        // Title/Label
            Text(
                text = title,
                modifier = Modifier
                    .weight(0.7f) // Take remaining space
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.h5
            )

        // Trailing Arrow Icon
        Icon(
            imageVector = Icons.Rounded.ChevronRight, // Replace with your arrow resource
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
