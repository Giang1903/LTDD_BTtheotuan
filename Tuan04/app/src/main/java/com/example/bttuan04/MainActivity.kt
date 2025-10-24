package com.example.bttuan04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bttuan04.navigation.LibraryScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // --- TRẠNG THÁI CHUNG (ĐÃ HOIST) ---
            var email by rememberSaveable { mutableStateOf("") }
            var code1 by rememberSaveable { mutableStateOf("") }
            var code2 by rememberSaveable { mutableStateOf("") }
            var code3 by rememberSaveable { mutableStateOf("") }
            var code4 by rememberSaveable { mutableStateOf("") }
            var code5 by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var confirmPassword by rememberSaveable { mutableStateOf("") }

            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") { SplashScreen(navController) }
                composable("onboarding") { OnboardingScreen(navController) }
                composable("home") { LibraryScreen() }

                // --- Flow quên mật khẩu ---
                composable("forget_password") {
                    ForgetPasswordScreen(
                        navController = navController,
                        email = email,
                        onEmailChange = { email = it }
                    )
                }
                composable("verification") {
                    VerificationScreen(
                        navController = navController,
                        email = email,
                        code1 = code1, onCode1Change = { code1 = it },
                        code2 = code2, onCode2Change = { code2 = it },
                        code3 = code3, onCode3Change = { code3 = it },
                        code4 = code4, onCode4Change = { code4 = it },
                        code5 = code5, onCode5Change = { code5 = it }
                    )
                }
                composable("reset_password") {
                    ResetPasswordScreen(
                        navController = navController,
                        password = password,
                        onPasswordChange = { password = it },
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = { confirmPassword = it }
                    )
                }
                composable("confirm_success") {
                    val code = "$code1$code2$code3$code4$code5"
                    ConfirmSuccessScreen(
                        navController = navController,
                        email = email,
                        code = code,
                        password = password
                    )
                }
            }
        }
    }
}

// --- CÁC MÀN HÌNH (Splash, Onboarding) ---

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "UTH Logo")
            Spacer(modifier = Modifier.height(24.dp))
            Text("UTH SmartTasks", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0069C0))
        }
    }
}

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pages = listOf(
        OnboardingPage(R.drawable.photo1, "Easy Time Management", "With management based on priority and daily tasks, it will give you convenience in managing and determining the tasks that must be done first."),
        OnboardingPage(R.drawable.photo2, "Increase Work Effectiveness", "Time management and the determination of important tasks will give your job statistics better and always improve."),
        OnboardingPage(R.drawable.photo3, "Reminder Notification", "The advantage of this application is that it also provides reminders for you so you don't forget to keep doing your assignments well and according to the time you have set.")
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "skip",
                color = Color.Gray,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("forget_password") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
            )
        }

        // Pager content
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val item = pages[page]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(painter = painterResource(id = item.imageRes), contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp))
                Spacer(modifier = Modifier.height(20.dp))
                Text(item.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(10.dp))
                Text(item.description, fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Gray)
            }
        }

        // Dots Indicator
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val color = if (pagerState.currentPage == index) Color(0xFF0069C0) else Color.LightGray
                Box(modifier = Modifier
                    .padding(4.dp)
                    .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                    .background(color, CircleShape))
            }
        }

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage > 0) {
                IconButton(
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF0069C0), contentColor = Color.White),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pages.lastIndex) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            navController.navigate("forget_password") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0069C0)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text(if (pagerState.currentPage == pages.lastIndex) "Get Started" else "Next", color = Color.White)
            }
        }
    }
}

// --- CÁC MÀN HÌNH (Flow Quên Mật Khẩu) ---

@Composable
fun ForgetPasswordScreen(
    navController: NavController,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(modifier = Modifier.padding(top = 40.dp))
        Spacer(modifier = Modifier.height(40.dp))

        AuthTitle(text = "Forget Password?")
        Spacer(modifier = Modifier.height(16.dp))
        AuthDescription(text = "Enter your Email, we will send you a verification code.")
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Your Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(text = "Next", onClick = {
            navController.navigate("verification")
        })
    }
}

@Composable
fun VerificationScreen(
    navController: NavController,
    email: String,
    code1: String, onCode1Change: (String) -> Unit,
    code2: String, onCode2Change: (String) -> Unit,
    code3: String, onCode3Change: (String) -> Unit,
    code4: String, onCode4Change: (String) -> Unit,
    code5: String, onCode5Change: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesters = remember { List(5) { FocusRequester() } }
    val codes = listOf(code1, code2, code3, code4, code5)
    val onCodeChanges = listOf(onCode1Change, onCode2Change, onCode3Change, onCode4Change, onCode5Change)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthBackButton(onClick = { navController.popBackStack() })

        AuthHeader(modifier = Modifier.padding(top = 24.dp))
        Spacer(modifier = Modifier.height(40.dp))

        AuthTitle(text = "Verify Code")
        Spacer(modifier = Modifier.height(16.dp))
        AuthDescription(text = "Enter the code we just sent you on your registered Email")
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            codes.forEachIndexed { index, code ->
                OtpTextField(
                    value = code,
                    onValueChange = { newText ->
                        onCodeChanges[index](newText)
                        if (newText.length == 1) {
                            if (index < 4) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { event ->
                            if (event.key == Key.Backspace &&
                                event.type == KeyEventType.KeyUp &&
                                code.isEmpty() &&
                                index > 0
                            ) {
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(text = "Next", onClick = {
            navController.navigate("reset_password")
        })
    }
}

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthBackButton(onClick = { navController.popBackStack() })

        AuthHeader(modifier = Modifier.padding(top = 24.dp))
        Spacer(modifier = Modifier.height(40.dp))

        AuthTitle(text = "Create new password")
        Spacer(modifier = Modifier.height(16.dp))
        AuthDescription(text = "Your new password must be different from previously used password")
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(text = "Next", onClick = {
            if (password.isNotEmpty() && password == confirmPassword) {
                navController.navigate("confirm_success")
            }
        })
    }
}

@Composable
fun ConfirmSuccessScreen(
    navController: NavController,
    email: String,
    code: String,
    password: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthBackButton(onClick = { navController.popBackStack() })

        AuthHeader(modifier = Modifier.padding(top = 24.dp))
        Spacer(modifier = Modifier.height(40.dp))

        AuthTitle(text = "Confirm")
        Spacer(modifier = Modifier.height(16.dp))
        AuthDescription(text = "We are here to help you!")
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {},
            readOnly = true,
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current,
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                disabledLeadingIconColor = LocalContentColor.current,
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code,
            onValueChange = {},
            readOnly = true,
            label = { Text("Verification Code") },
            leadingIcon = { Icon(Icons.Default.Dialpad, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current,
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                disabledLeadingIconColor = LocalContentColor.current,
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {},
            readOnly = true,
            label = { Text("New Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current,
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                disabledLeadingIconColor = LocalContentColor.current,
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(text = "Submit", onClick = {
            navController.navigate("home") {
                // Xóa back stack về trang gốc của flow này
                popUpTo("forget_password") { inclusive = true }
            }
        })
    }
}


// --- CÁC COMPOSABLE TÁI SỬ DỤNG (Auth) ---

@Composable
fun AuthHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "UTH Logo"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "SmartTasks",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0069C0)
        )
    }
}

@Composable
fun AuthTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun AuthDescription(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun AuthButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0069C0)),
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun AuthBackButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFF0069C0),
                contentColor = Color.White
            ),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 1 && it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        modifier = modifier.width(55.dp),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        )
    )
}