package com.example.bttuan05

import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import com.example.bttuan05.Navigation.*
import com.google.firebase.*
import com.google.firebase.auth.auth

@Composable
fun SplashScreen(navController: NavController) {
    val currentUser = Firebase.auth.currentUser

    LaunchedEffect(Unit) {
        delay(2000) // Chờ 2 giây
        val nextScreen = if (currentUser != null) {
            Screen.Profile.route // Đã đăng nhập
        } else {
            Screen.Login.route // Chưa đăng nhập
        }

        navController.navigate(nextScreen) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Thay R.drawable.uth_logo bằng logo của bạn
             Image(
                 painter = painterResource(id = R.drawable.uth_logo),
                 contentDescription = "UTH Logo",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "UTH SmartTasks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}