package com.example.remix.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val sku: String,
    val name: String,
    val presentation: String,
    val presentationGrams: Int,
    val realCatalogPrice: Double,
    val cost: Double,
    val targetMargin: Double,
    val stockUnits: Int
)
