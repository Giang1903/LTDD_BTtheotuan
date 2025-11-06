package com.example.btvntuan06.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvntuan06.data.ApiService
import com.example.btvntuan06.data.CreateTaskRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Trạng thái cho việc gọi API tạo task
sealed class AddTaskUiState {
    object Idle : AddTaskUiState()
    object Loading : AddTaskUiState()
    object Success : AddTaskUiState()
    data class Error(val message: String) : AddTaskUiState()
}

class AddTaskViewModel : ViewModel() {

    private val apiService = ApiService.create()

    // Trạng thái chung của việc gọi API
    private val _uiState = MutableStateFlow<AddTaskUiState>(AddTaskUiState.Idle)
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    // Trạng thái cho các trường nhập liệu
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _priority = MutableStateFlow("Medium")
    val priority: StateFlow<String> = _priority.asStateFlow()

    private val _dueDate = MutableStateFlow("")
    val dueDate: StateFlow<String> = _dueDate.asStateFlow()

    // Hàm cập nhật trạng thái từ UI
    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onCategoryChange(newCategory: String) {
        _category.value = newCategory
    }

    fun onDueDateChange(newDate: String) {
        _dueDate.value = newDate
    }

    // Hàm gọi API để tạo Task
    fun createTask() {
        viewModelScope.launch {
            _uiState.value = AddTaskUiState.Loading

            // Kiểm tra các trường bắt buộc (ví dụ: title)
            if (_title.value.isBlank()) {
                _uiState.value = AddTaskUiState.Error("Tiêu đề không được để trống")
                return@launch
            }

            // Tạo đối tượng request
            val request = CreateTaskRequest(
                title = _title.value,
                description = _description.value,
                category = _category.value.ifBlank { "Personal" }, // Giá trị mặc định
                priority = _priority.value,
                dueDate = _dueDate.value.ifBlank { "2025-12-31" } // Giá trị mặc định
            )

            try {
                // Gọi API
                val response = apiService.createTask(request)
                if (response.isSuccess) {
                    _uiState.value = AddTaskUiState.Success
                } else {
                    _uiState.value = AddTaskUiState.Error(response.message)
                }
            } catch (e: Exception) {
                _uiState.value = AddTaskUiState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }
}