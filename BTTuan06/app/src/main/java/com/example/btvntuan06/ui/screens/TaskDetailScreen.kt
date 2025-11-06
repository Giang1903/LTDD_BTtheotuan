package com.example.btvntuan06.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btvntuan06.data.*
import com.example.btvntuan06.viewmodel.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    viewModel: TaskDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onTaskDeleted: () -> Unit
) {
    LaunchedEffect(taskId) {
        viewModel.fetchTaskDetail(taskId)
    }

    val uiState by viewModel.uiState.collectAsState()

    // Biến để quản lý Dialog xác nhận xóa (MỚI)
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") }, // Sửa tiêu đề giống thiết kế
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    // Sửa lại để hiện Dialog
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, "Delete Task")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is TaskDetailUiState.Loading -> CircularProgressIndicator()
                // Sửa lại TaskDetailContent
                is TaskDetailUiState.Success -> TaskDetailContent(detail = state.taskDetail)
                is TaskDetailUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        // **HỘP THOẠI XÁC NHẬN XÓA (MỚI)**
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Xác nhận xóa") },
                text = { Text("Bạn có chắc chắn muốn xóa task này không?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteTask(taskId, onTaskDeleted)
                            showDeleteDialog = false
                        }
                    ) { Text("Xóa") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

// **BỐ CỤC NỘI DUNG MỚI HOÀN TOÀN (GIỐNG HỆT THIẾT KẾ)**
@Composable
private fun TaskDetailContent(detail: TaskDetail) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Tiêu đề
        item {
            Text(
                text = detail.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
        }

        // 1.5 Mô tả (Đưa lên trên)
        item {
            Text(
                text = detail.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
        }

        // 2. **HỘP THÔNG TIN MÀU HỒNG (MỚI)**
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                // Màu hồng giống thiết kế
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEBC6C5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoBoxItem("Category", detail.category, Icons.Filled.Bookmark)
                    InfoBoxItem("Status", detail.status, Icons.Filled.TaskAlt)
                    InfoBoxItem("Priority", detail.priority, Icons.Filled.Flag)
                }
            }
        }

        // 3. XÓA BỎ AsyncImage

        // 4. Các thông tin khác (Ngày tháng)
        item {
            Spacer(Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                InfoRow("Hạn chót", detail.dueDate.substringBefore("T"))
                InfoRow("Tạo lúc", detail.createdAt.substringBefore("T"))
                InfoRow("Cập nhật", detail.updatedAt.substringBefore("T"))
            }
            Spacer(Modifier.height(16.dp))
            Divider()
        }


        // 5. Subtasks (Giữ nguyên từ code cũ của bạn, đã tốt)
        if (detail.subtasks.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("Sub-tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(detail.subtasks) { subtask ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = subtask.isCompleted, onCheckedChange = null, enabled = false)
                    Text(
                        text = subtask.title,
                        textDecoration = if (subtask.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            item { Spacer(Modifier.height(8.dp)); Divider() }
        }

        // 6. Attachments (Giữ nguyên từ code cũ của bạn)
        if (detail.attachments.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("Tệp đính kèm", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(detail.attachments) { attachment ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Icon(Icons.Filled.AttachFile, contentDescription = "Tệp")
                    Spacer(Modifier.width(8.dp))
                    Text(text = attachment.fileName, color = MaterialTheme.colorScheme.primary)
                }
            }
            item { Spacer(Modifier.height(8.dp)); Divider() }
        }

        // 7. Reminders (Giữ nguyên từ code cũ của bạn)
        if (detail.reminders.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("Nhắc nhở", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(detail.reminders) { reminder ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Alarm, contentDescription = "Nhắc nhở")
                    Spacer(Modifier.width(8.dp))
                    Text("${reminder.time.substringBefore("T")} (${reminder.type})")
                }
            }
        }
    }
}

// **COMPOSABLE MỚI CHO HỘP THÔNG TIN**
@Composable
fun InfoBoxItem(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.DarkGray // Màu chữ đậm hơn
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Composable này vẫn được dùng
@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value)
    }
}