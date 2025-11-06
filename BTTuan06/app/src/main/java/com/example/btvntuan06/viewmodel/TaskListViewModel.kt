package com.example.btvntuan06.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvntuan06.data.ApiService
import com.example.btvntuan06.data.Task

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Trạng thái của màn hình danh sách
sealed class TaskListUiState {
    object Loading : TaskListUiState()
    data class Success(val tasks: List<Task>) : TaskListUiState()
    object Empty : TaskListUiState()
    data class Error(val message: String) : TaskListUiState()
}

class TaskListViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _uiState = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val uiState: StateFlow<TaskListUiState> = _uiState

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            _uiState.value = TaskListUiState.Loading
            try {
                // 1. Gọi API
                val response = apiService.getTasks()

                // 2. Kiểm tra phản hồi "bọc"
                if (response.isSuccess) {
                    val tasks = response.data // Lấy data từ bên trong
                    if (tasks.isEmpty()) {
                        _uiState.value = TaskListUiState.Empty
                    } else {
                        _uiState.value = TaskListUiState.Success(tasks)
                    }
                } else {
                    // Lấy thông báo lỗi từ API
                    _uiState.value = TaskListUiState.Error(response.message)
                }
            } catch (e: Exception) {
                // Bắt lỗi (ví dụ: mất mạng, lỗi JSON)
                _uiState.value = TaskListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // **HÀM MỚI ĐỂ XỬ LÝ CHECKBOX**
    fun toggleTaskCompletion(taskId: Int) {
        val currentState = _uiState.value
        if (currentState is TaskListUiState.Success) {

            // Tạo một danh sách mới với task đã được cập nhật
            val newTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    // Trả về một bản sao của task với isCompleted bị đảo ngược
                    task.copy(isCompleted = !task.isCompleted)
                } else {
                    task
                }
            }

            // Cập nhật UI State
            _uiState.update {
                TaskListUiState.Success(newTasks)
            }

            // Lưu ý: Trạng thái này chỉ là tạm thời.
            // Nếu API không hỗ trợ cập nhật, nó sẽ bị reset khi tải lại.
        }
    }
}