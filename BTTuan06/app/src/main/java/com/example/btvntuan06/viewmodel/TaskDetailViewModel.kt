package com.example.btvntuan06.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvntuan06.data.ApiService
import com.example.btvntuan06.data.TaskDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Trạng thái của màn hình chi tiết
sealed class TaskDetailUiState {
    object Loading : TaskDetailUiState()
    data class Success(val taskDetail: TaskDetail) : TaskDetailUiState()
    data class Error(val message: String) : TaskDetailUiState()
}

class TaskDetailViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _uiState = MutableStateFlow<TaskDetailUiState>(TaskDetailUiState.Loading)
    val uiState: StateFlow<TaskDetailUiState> = _uiState

    fun fetchTaskDetail(taskId: String) {
        viewModelScope.launch {
            _uiState.value = TaskDetailUiState.Loading
            try {
                // 1. Gọi API
                val response = apiService.getTaskDetail(taskId)

                // 2. Kiểm tra và mở đối tượng bọc
                if (response.isSuccess) {
                    _uiState.value = TaskDetailUiState.Success(response.data) // Lấy data
                } else {
                    _uiState.value = TaskDetailUiState.Error(response.message)
                }
            } catch (e: Exception) {
                _uiState.value = TaskDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Hàm để xóa Task
    fun deleteTask(taskId: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteTask(taskId)
                if (response.isSuccessful) {
                    onDeleted()
                } else {
                    // Xử lý lỗi
                }
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }
}