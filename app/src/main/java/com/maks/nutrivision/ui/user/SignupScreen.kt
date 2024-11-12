package com.maks.nutrivision.ui.user

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.maks.nutrivision.ui.common.CheckboxComponent
import com.maks.nutrivision.ui.common.HeadingTextComponent
import com.maks.nutrivision.ui.common.MyTextFieldComponent
import com.maks.nutrivision.ui.common.NormalTextComponent
import com.maks.nutrivision.ui.common.PasswordTextFieldComponent
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.Primary

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SignupScreen(navController: NavHostController,
                 userViewModel: UserViewModel = hiltViewModel()) {
    var state =  userViewModel.authState

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NormalTextComponent(value = "Hello there,")
                HeadingTextComponent(value = "Create an Account")
                Spacer(modifier = Modifier.height(25.dp))

                val fname = remember { mutableStateOf(String()) }
                val lname = remember { mutableStateOf(String()) }
                val email = remember { mutableStateOf(String()) }
                val mobile = remember { mutableStateOf(String()) }
                val password = remember { mutableStateOf(String()) }
                val confirmPassword = remember { mutableStateOf(String()) }
                val address = remember { mutableStateOf(String()) }
                val pincode = remember { mutableStateOf(String()) }

                Column {
                    MyTextFieldComponent(
                        labelValue = "First Name",
                        icon = Icons.Outlined.Person,
                        onValueChange = { fname.value = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MyTextFieldComponent(
                        labelValue = "Last Name",
                        icon = Icons.Outlined.Person,
                        onValueChange = { lname.value = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MyTextFieldComponent(
                        labelValue = "Email",
                        icon = Icons.Outlined.Email,
                        onValueChange = { email.value = it }

                    )
                    MyTextFieldComponent(
                        labelValue = "Mobile No.",
                        icon = Icons.Outlined.Phone, onValueChange = { mobile.value = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PasswordTextFieldComponent(
                        labelValue = "Password",
                        icon = Icons.Outlined.Lock, onValueChange = { password.value = it }
                    )
                    PasswordTextFieldComponent(
                        labelValue = "Confirm Password",
                        icon = Icons.Outlined.Lock, onValueChange = { confirmPassword.value = it }
                    )
                    MyTextFieldComponent(
                        labelValue = "Address",
                        icon = Icons.Outlined.Home, onValueChange = { address.value = it }
                    )
                    MyTextFieldComponent(
                        labelValue = "Pincode",
                        icon = Icons.Outlined.Pin, onValueChange = { pincode.value = it }
                    )
                    CheckboxComponent()
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                userViewModel.signup(
                                    {
                                        navController.navigate(Screen.Login.route)
                                    },
                                    fname = fname.value,
                                    lname = lname.value,
                                    email = email.value,
                                    mobile = mobile.value,
                                    password = password.value,
                                    confirmPassword = confirmPassword.value,
                                    address = address.value,
                                    pincode = pincode.value
                                )
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonColors(Primary, Color.White, Primary, Primary)
                        ) {
                            Text(
                                text = "Register", style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Default
                                )
                            )
                        }
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (state.errorMsg.contains("success")==true) {
                Toast.makeText(LocalContext.current, "User registration Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Login.route)
            }
            if (state.errorMsg.contains("Error")==true) {
                Toast.makeText(LocalContext.current, state.errorMsg, Toast.LENGTH_LONG).show()
            }
    }
    }
}