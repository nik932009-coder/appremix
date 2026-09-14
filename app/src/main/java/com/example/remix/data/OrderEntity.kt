package com.example.remix.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val date: Long,
    val totalAmount: Double,
    val totalCost: Double,
    val status: String,
    val channel: String
)
