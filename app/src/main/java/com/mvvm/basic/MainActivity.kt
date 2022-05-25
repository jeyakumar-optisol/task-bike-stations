package com.mvvm.basic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvvm.basic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initObserver()
        initListener()
        initPreview()
    }

    private fun initData() {
    }

    private fun initObserver() {

    }

    private fun initListener() {
    }

    private fun initPreview() {
    }
}