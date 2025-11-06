package com.example.btvntuan06.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btvntuan06.viewmodel.*
import com.example.btvntuan06.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = viewModel(),
    onTaskClick: (String) -> Unit, // Callback để điều hướng, truyền đi Task ID (String)
    onAddTaskClick: () -> Unit // <-- THÊM THAM SỐ NÀY
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            AppBottomNavigation()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTaskClick() } // <-- CẬP NHẬT Ở ĐÂY
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Thêm Task")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is TaskListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is TaskListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.tasks, key = { it.id }) { task ->
                            TaskItem(
                                task = task,
                                onTaskClick = { onTaskClick(task.id.toString()) },
                                onCheckedChange = {
                                    viewModel.toggleTaskCompletion(task.id)
                                }
                            )
                        }
                    }
                }
                is TaskListUiState.Empty -> {
                    EmptyScreen(modifier = Modifier.align(Alignment.Center))
                }
                is TaskListUiState.Error -> {
                    Text(
                        text = "Lỗi: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick() },
        colors = CardDefaults.cardColors(
            containerColor = getTaskColor(task.category) // Màu nền động
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Status: ${task.status}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Due: ${task.dueDate.substringBefore("T")}", // Cắt bỏ phần giờ
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.EventBusy,
            contentDescription = "Rỗng",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Tasks Yet!",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Click the '+' button to add a new task.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun getTaskColor(category: String): Color {
    // Dựa trên dữ liệu JSON bạn cung cấp
    return when (category.lowercase()) {
        "work" -> Color(0xFFEBC6C5) // Màu hồng
        "personal" -> Color(0xFFD4E9C6) // Màu xanh nhạt
        "meeting" -> Color(0xFFC6E0E9) // Màu xanh da trời
        "fitness" -> Color(0xFFC6E0E9) // Xanh da trời
        "travel" -> Color(0xFFE8D4C6) // Nâu nhạt
        "shopping" -> Color(0xFFC6CDE9) // Tím nhạt
        "education" -> Color(0xFFE9E4C6) // Vàng nhạt
        "finance" -> Color(0xFFC6E9D7) // Teal nhạt
        "hobby" -> Color(0xFFE9C6E4) // Magenta nhạt
        else -> Color(0xFFE0E0E0) // Màu xám nhạt
    }
}

// **COMPOSABLE MỚI CHO BOTTOM BAR**
@Composable
fun AppBottomNavigation() {
    NavigationBar {
        // "selected" = true sẽ làm cho mục đó sáng lên
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Trang chủ") },
            label = { Text("Home") },
            selected = true, // Tạm thời chọn "Home"
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Tasks") },
            label = { Text("Tasks") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Cài đặt") },
            label = { Text("Settings") },
            selected = false,
            onClick = { /* TODO */ }
        )
    }
}