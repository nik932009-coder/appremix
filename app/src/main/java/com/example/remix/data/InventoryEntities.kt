package com.example.remix.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey
    val id: String,
    val sku: String,
    val date: Long,
    val quantity: Int,
    val totalCost: Double
)

@Entity(tableName = "shrinkage")
data class ShrinkageEntity(
    @PrimaryKey
    val id: String,
    val sku: String,
    val date: Long,
    val quantity: Int,
    val reason: String,
    val costValue: Double
)
