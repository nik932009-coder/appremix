package com.example.remix.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val whatsapp: String,
    val firstPurchaseDate: Long?,
    val lastPurchaseDate: Long?,
    val orderCount: Int,
    val totalSpent: Double,
    val notes: String
)
