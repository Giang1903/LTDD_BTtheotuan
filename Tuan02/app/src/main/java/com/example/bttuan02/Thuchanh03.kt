package com.example.bttuan02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.util.Patterns
import android.widget.Toast
import com.example.bttuan02.databinding.ActivityThuchanh03Binding

class Thuchanh03 : AppCompatActivity() {

    private lateinit var binding: ActivityThuchanh03Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo View Binding
        binding = ActivityThuchanh03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập sự kiện click cho nút "Kiểm tra"
        binding.btnCheck.setOnClickListener {
            checkEmailFormat()
        }
    }

    /**
     * Hàm kiểm tra định dạng email và hiển thị thông báo lỗi.
     */
    private fun checkEmailFormat() {
        // Lấy chuỗi email từ EditText
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty()) {
            // Trường hợp Email rỗng
            binding.tvError.text = "Vui lòng nhập Email"
            binding.tvError.visibility = View.VISIBLE
            return
        }

        // Sử dụng Patterns.EMAIL_ADDRESS để kiểm tra định dạng chuẩn
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Email hợp lệ
            binding.tvError.visibility = View.GONE
            Toast.makeText(this, "Email hợp lệ!", Toast.LENGTH_SHORT).show()
        } else {
            // Email không đúng định dạng (Trạng thái trong hình)
            binding.tvError.text = "Email không đúng định dạng"
            binding.tvError.visibility = View.VISIBLE
        }
    }
}