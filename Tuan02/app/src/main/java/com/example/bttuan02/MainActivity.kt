package com.example.bttuan02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
            // thuchanh01
        val thuchanh01 = findViewById<Button>(R.id.thuchanh01)
        thuchanh01.setOnClickListener {
            val intent = Intent(this, Thuchanh01::class.java)
            startActivity(intent)
        }
        // thuchanh02
        val thuchanh02 = findViewById<Button>(R.id.thuchanh02)
        thuchanh02.setOnClickListener {
            val intent = Intent(this, Thuchanh02::class.java)
            startActivity(intent)
        }
        val thuchanh03 = findViewById<Button>(R.id.thuchanh03)
        thuchanh03.setOnClickListener {
            val intent = Intent(this, Thuchanh03::class.java)
            startActivity(intent)
        }

    }
}