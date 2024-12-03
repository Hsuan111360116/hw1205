package com.example.lab13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // 頻道常量
    companion object {
        const val CHANNEL_MUSIC = "music"
        const val CHANNEL_NEWS = "new"
        const val CHANNEL_SPORT = "sport"
    }

    // 聲明界面元素
    private lateinit var tvMsg: TextView
    private lateinit var btnMusic: Button
    private lateinit var btnNew: Button
    private lateinit var btnSport: Button

    // 追蹤接收器是否已註冊
    private var isReceiverRegistered = false

    // 廣播接收器
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let {
                val message = it.getString("msg") ?: "No message"
                tvMsg.text = message
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 初始化界面元素
        initializeViews()

        // 設置系統欄內邊距
        setupWindowInsets()

        // 設置按鈕監聽器
        setupButtonListeners()
    }

    // 初始化界面元素
    private fun initializeViews() {
        tvMsg = findViewById(R.id.tvMsg)
        btnMusic = findViewById(R.id.btnMusic)
        btnNew = findViewById(R.id.btnNew)
        btnSport = findViewById(R.id.btnSport)
    }

    // 設置系統欄內邊距
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 設置按鈕監聽器
    private fun setupButtonListeners() {
        btnMusic.setOnClickListener { registerAndStartService(CHANNEL_MUSIC) }
        btnNew.setOnClickListener { registerAndStartService(CHANNEL_NEWS) }
        btnSport.setOnClickListener { registerAndStartService(CHANNEL_SPORT) }
    }

    // 註冊服務和廣播接收器
    private fun registerAndStartService(channel: String) {
        // 如果已經註冊，先解除
        if (isReceiverRegistered) {
            unregisterReceiver(receiver)
            isReceiverRegistered = false
        }

        // 創建意圖過濾器
        val intentFilter = IntentFilter(channel)

        // 註冊接收器（根據 Android 版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }

        // 標記已註冊
        isReceiverRegistered = true

        // 啟動服務
        val serviceIntent = Intent(this, MyService::class.java)
        serviceIntent.putExtra("channel", channel)
        startService(serviceIntent)
    }

    // 在活動銷毀時解除接收器
    override fun onDestroy() {
        if (isReceiverRegistered) {
            unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }
}