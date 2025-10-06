package com.example.aqualumeapp.models


data class WaterLog(
    val id: Long = System.currentTimeMillis(),
    val amount: Int, // in ml
    val timestamp: Long = System.currentTimeMillis()
)
