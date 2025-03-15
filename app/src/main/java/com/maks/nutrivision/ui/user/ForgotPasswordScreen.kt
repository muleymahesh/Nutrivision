import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maks.nutrivision.R
import com.maks.nutrivision.ui.common.Screen
import com.maks.nutrivision.ui.theme.Primary
import com.maks.nutrivision.ui.user.UserViewModel

@Composable
fun ForgotPasswordScreen(navController: NavHostController, userViewModel: UserViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val users = userViewModel.authState
    var isEmailError by remember { mutableStateOf(false) }
    var isMobileError by remember { mutableStateOf(false) }
    var isEmailOrMobileError by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidMobile(phone: String): Boolean = phone.length == 10 && phone.all { it.isDigit() }
    if (users.isLogged){
        navController.popBackStack()
    }
    Scaffold(
        topBar = {

            TopAppBar(
                backgroundColor = Primary,
                elevation = 16.dp,
                title = { Text("Forgot Password?") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Forgot Password?", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Your Email/mobile number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                if (isEmailOrMobileError) {
                    Text(
                        text = "Invalid Email or Mobile Number",
                        color = MaterialTheme.colors.error
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        isEmailOrMobileError = false
                        val emailOrMobile = email.text
                        if (emailOrMobile.isNotEmpty()) {
                            if (isValidEmail(emailOrMobile)) {
                                isEmailError = false
                            } else if (isValidMobile(emailOrMobile)) {
                                isMobileError = false
                            } else {
                                isEmailOrMobileError = true
                            }
                            if (isEmailOrMobileError) {
                                return@Button
                            }
                            userViewModel.forgotPassword(email.text)

                        }else{
                            isEmailOrMobileError = true
                        }

                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Primary)
                ) {
                    Text(modifier = Modifier.padding(8.dp),
                        text = "Submit",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (users.isLoading) {
                    CircularProgressIndicator()
                }
                if (users.errorMsg.isNotEmpty()) {
                    Text(text = users.errorMsg, color = MaterialTheme.colors.error)
                }
                if (message.isNotEmpty()) {
                    Text(text = message, color = MaterialTheme.colors.error)
                }
            }
        })
    }

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(navController = rememberNavController())
}