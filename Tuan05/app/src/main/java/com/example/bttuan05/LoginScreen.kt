package com.example.bttuan05
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.bttuan05.Navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // --- Logic Firebase Google Sign-In ---
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("87348684523-intnh2q9ttptg0ld12l525ve4t3btoee.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val authResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                coroutineScope.launch {
                    isLoading = true // Bật loading
                    firebaseAuthWithGoogle(account.idToken!!) { success, error ->
                        isLoading = false // Tắt loading
                        if (success) {
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message = "Google Sign-In Failed: ${error ?: "Unknown error"}")
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                isLoading = false
                Log.w("LoginScreen", "Google sign in failed", e)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = "Google sign in failed: ${e.message}")
                }
            }
        } else {
            isLoading = false
            Log.w("LoginScreen", "Google Sign-In cancelled by user.")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = "Google Sign-In cancelled.")
            }
        }
    }

    // --- UI
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 2. Hộp gradient màu xanh
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFE0F7FA), // Màu xanh nhạt
                                    Color(0xFFB2EBF2)  // Màu xanh đậm hơn
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.ut_logo),
                            contentDescription = "UTH Logo",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "SmartTasks",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF005A9C) // Màu xanh UTH
                        )
                        Text(
                            text = "A simple and efficient to-do app",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // 3. Nội dung bên dưới
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Welcome",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ready to explore? Log in to get started.",
                        fontSize = 16.sp,
                        color = Color.Gray, // <-- ĐÃ SỬA
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Nút Google Sign-In
                    Button(
                        onClick = {
                            isLoading = true
                            authResultLauncher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp), // Bo góc
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp) // Đổ bóng
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "SIGN IN WITH GOOGLE",
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Đẩy footer xuống dưới

                // 4. Footer
                Text(
                    text = "© UTHSmartTasks",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}
private suspend fun firebaseAuthWithGoogle(idToken: String, callback: (Boolean, String?) -> Unit) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    try {
        Firebase.auth.signInWithCredential(credential).await()
        callback(true, null)
    } catch (e: Exception) {
        callback(false, e.message)
    }
}