package com.example.btvntuan06.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btvntuan06.viewmodel.AddTaskUiState
import com.example.btvntuan06.viewmodel.AddTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    // Lắng nghe các trạng thái từ ViewModel
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val category by viewModel.category.collectAsState()
    val dueDate by viewModel.dueDate.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Xử lý khi API thành công -> tự động quay lại
    LaunchedEffect(uiState) {
        if (uiState is AddTaskUiState.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Cho phép cuộn nếu chật
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hiển thị lỗi (nếu có)
                if (uiState is AddTaskUiState.Error) {
                    Text(
                        text = (uiState as AddTaskUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = { Text("Title (Bắt buộc)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState is AddTaskUiState.Error && title.isBlank()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { viewModel.onCategoryChange(it) },
                    label = { Text("Category (vd: Work, Personal)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { viewModel.onDueDateChange(it) },
                    label = { Text("Due Date (vd: 2025-12-31)") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.weight(1f)) // Đẩy nút Save xuống dưới

                Button(
                    onClick = {
                        // Chỉ gọi API khi không đang trong trạng thái Loading
                        if (uiState !is AddTaskUiState.Loading) {
                            viewModel.createTask()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AddTaskUiState.Loading // Vô hiệu hóa nút khi đang tải
                ) {
                    if (uiState is AddTaskUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("SAVE TASK")
                    }
                }
            }
        }
    )
}