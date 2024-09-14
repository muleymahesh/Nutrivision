package com.maks.nutrivision.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maks.nutrivision.ui.common.Screen

@Composable
fun EditProfileScreen(navController: NavHostController,userViewModel: UserViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf(TextFieldValue("Rashmi")) }
    var email by remember { mutableStateOf(TextFieldValue("rashmi@example.com")) }
    var mobile by remember { mutableStateOf(TextFieldValue("+91 9876543210")) }
    var address by remember { mutableStateOf(TextFieldValue("123, Main Street, City, Country")) }
    LaunchedEffect(key1 = true) {
        userViewModel.getAllUsers()
    }
    val profile = userViewModel.profileList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        ProfileImage()

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        EditableTextField1(label = "Name", value = name, onValueChange = { name = it })

        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        EditableTextField1(label = "Email", value = email, onValueChange = { email = it })

        Spacer(modifier = Modifier.height(8.dp))

        // Mobile Field
        EditableTextField1(label = "Mobile", value = mobile, onValueChange = { mobile = it })

        Spacer(modifier = Modifier.height(8.dp))

        // Address Field
        EditableTextField1(label = "Address", value = address, onValueChange = { address = it })

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { /* Handle save logic */ }) {
            Text(text = "Save Changes")
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
