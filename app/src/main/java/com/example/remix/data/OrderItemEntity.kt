package com.example.remix.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey
    val id: String,
    val orderId: String,
    val sku: String,
    val quantity: Int,
    val unitPrice: Double,
    val unitCost: Double
)
