package com.maks.nutrivision.ui.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.maks.nutrivision.ui.theme.Primary
import androidx.hilt.navigation.compose.hiltViewModel
import com.maks.nutrivision.ui.common.HeadingTextComponent
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.Screen


@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        userViewModel.getAllUsers()
    }
    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`

        Column(
            Modifier
                .background(Color.White)
                .fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ClickableText(
                    text = AnnotatedString("Sign up here"),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp),
                    onClick = { navController.navigate(Screen.Register.route) },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Primary
                    )
                )
            }
        }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            val context = LocalContext.current
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }

        NormalTextComponent(value = "Hello there,")
        HeadingTextComponent(value = "Enter your credentials")


        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextFieldBackground(Color.White) {
            OutlinedTextField(
                label = { Text(text = "Email/mobile") },
                value = username.value,
                onValueChange = { username.value = it })
        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextFieldBackground(Color.White) {
            OutlinedTextField(
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { userViewModel.login(email = username.value.text, password = password.value.text)},
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonColors(Primary, Color.White, Primary, Primary)
            ) {
                Text(text = "Login" , style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default
                ))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { },
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )
        val users = userViewModel.authState.collectAsState()
        if (users.value.response?.result?.contains("success") == true) {
            navController.popBackStack()
            navController.navigate(Screen.Profile.route)
        }else if (users.value.response?.result?.contains("fail") == true) {
            Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show()
        }

        val profileList = userViewModel.profileList.collectAsState()
        if (profileList.value.isNotEmpty()) {
            navController.popBackStack()
            navController.navigate(Screen.Profile.route)
        }
        if (users.value.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }    }
}

@Composable
fun OutlinedTextFieldBackground(
    color: Color,
    content: @Composable () -> Unit
) {
    // This box just wraps the background and the OutlinedTextField
    Box {
        // This box works as background
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 8.dp) // adding some space to the label
                .background(
                    color,
                    // rounded corner to match with the OutlinedTextField
                    shape = RoundedCornerShape(4.dp)
                )
        )
        // OutlineTextField will be the content...
        content()
    }
}