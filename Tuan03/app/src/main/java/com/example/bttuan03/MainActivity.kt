package com.example.bttuan03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import androidx.compose.material.icons.*
import androidx.navigation.NavHostController
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.draw.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.example.bttuan03.ui.theme.BTtuan03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BTtuan03Theme {
                UIComponentsApp()
            }
        }
    }
}

// ----------------------------------------------
// App Navigation
// ----------------------------------------------
@Composable
fun UIComponentsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "intro") {
        composable("intro") { IntroScreen(navController) }
        composable("list") { ComponentListScreen(navController) }
        composable("text") { TextDetailScreen() }
        composable("image") { ImageDetailScreen() }
        composable("textfield") { TextFieldScreen() }
        composable("row") { RowLayoutScreen() }
        composable("column") { ColumnLayoutScreen() }
        composable("box") { BoxLayoutScreen() }
    }
}

// ----------------------------------------------
// Intro Screen
// ----------------------------------------------
@Composable
fun IntroScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Thông tin người dùng
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Nguyễn Văn A",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "2342312323",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Phần nội dung giữa màn hình
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Jetpack Compose",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }

            // Nút ở cuối màn hình
            Button(
                onClick = { navController.navigate("list") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text("I’m ready", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

// Component List Screen
@Composable
fun ComponentListScreen(navController: NavHostController) {
    val uiSections = listOf(
        "Display" to listOf(
            "Text" to "Displays text",
            "Image" to "Displays an image"
        ),
        "Input" to listOf(
            "TextField" to "Input field for text",
            "PasswordField" to "Input field for passwords"
        ),
        "Layout" to listOf(
            "Column" to "Arranges elements vertically",
            "Row" to "Arranges elements horizontally"
        )
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "UI Components List",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))

            uiSections.forEach { (section, components) ->
                Text(
                    section,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
                components.forEach { (label, desc) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFD6E9FF), RoundedCornerShape(10.dp))
                            .clickable {
                                when (label) {
                                    "Text" -> navController.navigate("text")
                                    "Image" -> navController.navigate("image")
                                    "TextField" -> navController.navigate("textfield")
                                    "PasswordField" -> navController.navigate("textfield")
                                    "Row" -> navController.navigate("row")
                                    "Column" -> navController.navigate("column")
                                    "Box" -> navController.navigate("box")
                                }
                            }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text(desc, fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Ô "Tự tìm hiểu"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD6E9FF), RoundedCornerShape(10.dp))
                    .clickable { navController.navigate("box") }
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        "Box Layout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "Box Layout",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

// Text Detail
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Text Detail",
                        color = Color.Blue,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* navController.popBackStack() nếu cần */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Blue
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // căn giữa nội dung
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                buildAnnotatedString {
                    append("The ")
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append("quick")
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFB8860B),
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Brown") }
                    append("\nfox j u m p s ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    ) { append("over") }
                    append("\nthe ")
                    withStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            fontStyle = FontStyle.Italic
                        )
                    ) { append("lazy") }
                    append(" dog.")
                },
                fontSize = 42.sp,
                lineHeight = 42.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.width(240.dp))
        }
    }
}

// Image Detail
@Composable
fun ImageDetailScreen() {
    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Ảnh từ đường link
            AsyncImage(
                model = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fgiaothongvantaitphcm.edu.vn%2F&psig=AOvVaw1fJGbHlJ4WrvZ3r30Nkv2m&ust=1761226906049000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCIC4x9L3t5ADFQAAAAAdAAAAABAp",
                contentDescription = "Ảnh online",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                "https://uth.edu.vn/",
                fontSize = 12.sp
            )

            Spacer(Modifier.height(10.dp))

            // Ảnh trong app
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "UTH",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Text("In-app", fontSize = 12.sp)
        }
    }
}

// TextField Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldScreen() {
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TextField",
                        color = Color(0xFF2196F3), // Màu xanh
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { /* handle back */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2196F3)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),

            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding( top =240.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Thông tin nhập") },
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tự động cập nhật dữ liệu theo textfield",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Row Layout
@Composable
fun RowLayoutScreen() {
    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(3) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(4) {
                        Box(
                            Modifier
                                .size(80.dp)
                                .background(Color(0xFFB3C7FF), RoundedCornerShape(12.dp))
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// Column Layout

@Composable
fun ColumnLayoutScreen() {
    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top= 60.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(3) {
                Box(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(120.dp)
                        .background(Color(0xFF99E2A0), RoundedCornerShape(12.dp))
                )
            }
        }
    }
}
@Composable
fun BoxLayoutScreen() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            // Ảnh nền mờ
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f),
                contentScale = ContentScale.Crop
            )

            // Ảnh avatar ở giữa
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
                    .background(Color.White, RoundedCornerShape(80.dp))
                    .padding(20.dp)
            )

            // Tiêu đề chồng phía trên
            Text(
                text = "Box Layout Example",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp)
            )

            // Nút chồng ở góc phải dưới
            Button(
                onClick = { /* hành động tùy ý */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Text("Action", color = Color.White)
            }

            // Text ở góc trái dưới
            Text(
                text = "Overlay demo with Box",
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                fontStyle = FontStyle.Italic
            )
        }
    }
}