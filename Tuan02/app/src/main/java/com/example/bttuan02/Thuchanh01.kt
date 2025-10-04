package com.example.bttuan02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import com.example.bttuan02.databinding.ActivityThuchanh01Binding

class Thuchanh01 : AppCompatActivity() {

    private lateinit var binding: ActivityThuchanh01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo View Binding
        binding = ActivityThuchanh01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập sự kiện click cho nút "Kiểm tra"
        binding.btnCheck.setOnClickListener {
            processCheckAge()
        }
    }

    /**
     * Hàm xử lý lấy dữ liệu, kiểm tra và hiển thị kết quả.
     */
    private fun processCheckAge() {
        // 1. Lấy dữ liệu
        val name = binding.etName.text.toString().trim()
        val ageString = binding.etAge.text.toString().trim()
        val age: Int? = ageString.toIntOrNull()

        // 2. Kiểm tra lỗi nhập liệu cơ bản
        if (name.isEmpty() || ageString.isEmpty() || age == null || age < 0) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Tên và Tuổi hợp lệ!", Toast.LENGTH_LONG).show()
            return
        }

        // 3. Phân loại theo Tuổi
        val ageCategory = when (age) {
            in 0..2 -> "Em bé"             // Tuổi <= 2
            in 3..6 -> "Trẻ em"            // Tuổi 3 - 6
            in 7..65 -> "Người lớn"        // Tuổi 7 - 65
            else -> "Người già"            // Tuổi > 65
        }

        // 4. Hiển thị kết quả
        val resultMessage = "$name ($age tuổi) thuộc nhóm: $ageCategory"
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()

    }
}