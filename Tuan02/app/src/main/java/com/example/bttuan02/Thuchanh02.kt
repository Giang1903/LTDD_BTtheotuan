package com.example.bttuan02

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.bttuan02.databinding.ActivityThuchanh02Binding

class Thuchanh02 : AppCompatActivity() {

    private lateinit var binding: ActivityThuchanh02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThuchanh02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val inputText = binding.etNumber.text.toString().trim()

            // Xóa nút cũ
            binding.llButtonsContainer.removeAllViews()

            // Trường hợp 1: không nhập gì
            if (inputText.isEmpty()) {
                binding.tvError.text = "Vui lòng nhập số lượng!"
                return@setOnClickListener
            }

            // Trường hợp 2: nhập chữ hoặc ký tự không phải số
            val number = inputText.toIntOrNull()
            if (number == null) {
                binding.tvError.text = "Dữ liệu bạn nhập không hợp lệ!"
                return@setOnClickListener
            }

            // Trường hợp 3: nhập số <= 0
            if (number <= 0) {
                binding.tvError.text = "Vui lòng nhập số nguyên dương!"
                return@setOnClickListener
            }

            // Nếu hợp lệ
            binding.tvError.text = ""

            // Tạo các nút động
            for (i in 1..number) {
                val button = Button(this)
                button.text = i.toString()
                button.setTextColor(Color.WHITE)
                button.textSize = 18f
                button.setPadding(10, 10, 10, 10)
                button.background = getDrawable(R.drawable.edittext_bgh)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 10, 0, 0)
                button.layoutParams = params
                button.gravity = Gravity.CENTER

                binding.llButtonsContainer.addView(button)
            }
        }
    }
}

