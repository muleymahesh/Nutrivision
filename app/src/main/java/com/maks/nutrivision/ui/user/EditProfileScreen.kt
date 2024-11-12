package com.maks.nutrivision.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maks.nutrivision.R
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.Primary

@Composable
fun EditProfileScreen(navController: NavHostController,userViewModel: UserViewModel = hiltViewModel()) {

    val state = userViewModel.authState

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
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
                androidx.compose.material.OutlinedButton(
                    onClick = {  },
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Primary)
                ) {
                    androidx.compose.material.Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Save",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }
        }
    ) { contentPadding ->
        Column(

            modifier = Modifier
                .background(Color.White)
                .fillMaxSize().verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            ProfileImage()

            Spacer(modifier = Modifier.height(16.dp).padding(contentPadding))

            // Name Field
            EditableTextField1(label = "Name", value = TextFieldValue(userViewModel.name), onValueChange = { name -> userViewModel.updateName(name.text) })

            Spacer(modifier = Modifier.height(8.dp))

            // Email Field
            EditableTextField1(label = "Email", value = TextFieldValue(userViewModel.email), onValueChange = { email -> userViewModel.updateEmail(email.text) })

            Spacer(modifier = Modifier.height(8.dp))

            // Mobile Field
            EditableTextField1(label = "Mobile", value = TextFieldValue(userViewModel.mobile), onValueChange = { mobile ->userViewModel.updateMobile(mobile.text) })

                       // Address Field
            EditableTextField1(label = "Address", value = TextFieldValue(userViewModel.address), onValueChange = { address ->userViewModel.updateAddress(address.text) })

            EditableTextField1(label = "Pincode", value = TextFieldValue(userViewModel.pincode), onValueChange = { pincode ->userViewModel.updatePincode(pincode.text) })

            Spacer(modifier = Modifier.height(44.dp))

        }
    }
}
@Composable
fun EditableTextField1(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
    )
}
