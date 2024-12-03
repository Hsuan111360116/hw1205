package com.example.lab12

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 使用 ViewCompat 設置窗口插入監聽器
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 為按鈕設定監聽器，使用 findViewById
        findViewById<android.widget.Button>(R.id.btnStart).setOnClickListener {
            // 啟動 Service
            startService(Intent(this, MyService::class.java))

            // 顯示 Toast 訊息
            Toast.makeText(this, "啟動Service", Toast.LENGTH_SHORT).show()

            // 關閉 Activity
            finish()
        }
    }
}