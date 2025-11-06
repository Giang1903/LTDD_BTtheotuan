package com.example.btvntuan06.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body // <-- THÊM IMPORT
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST // <-- THÊM IMPORT
import retrofit2.http.Path

interface ApiService {

    // Trả về đối tượng bọc chứa danh sách Task
    @GET("api/researchUTH/tasks")
    suspend fun getTasks(): ApiResponse<List<Task>>

    // Trả về đối tượng bọc chứa một TaskDetail
    @GET("api/researchUTH/task/{taskId}")
    suspend fun getTaskDetail(@Path("taskId") taskId: String): ApiResponse<TaskDetail>

    // **HÀM MỚI ĐỂ TẠO TASK**
    // (Giả định server trả về Task đã tạo, bọc trong ApiResponse)
    @POST("api/researchUTH/tasks")
    suspend fun createTask(@Body task: CreateTaskRequest): ApiResponse<Task>

    @DELETE("api/researchUTH/task/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String): Response<Unit>

    companion object {
        private const val BASE_URL = "https://amock.io/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}