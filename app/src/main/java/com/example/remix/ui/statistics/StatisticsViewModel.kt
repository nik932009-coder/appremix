package com.example.remix.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remix.data.RemixRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import android.content.Context

class StatisticsViewModel(private val repository: RemixRepository, private val context: Context) : ViewModel() {
    // Total sales amount
    val totalSales: StateFlow<Double> = repository.totalSales
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Total cost amount
    val totalCost: StateFlow<Double> = repository.totalCost
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Number of orders
    val orderCount: StateFlow<Int> = repository.orderCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Simple profit calculation
    val profit: StateFlow<Double> = repository.totalSales.map { sales ->
        val cost = repository.totalCost.value
        sales - cost
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /**
     * Export all orders to CSV in the app's files directory.
     * Returns the absolute path of the created file.
     */
    fun exportOrdersCsv(): String {
        val csvFile = File(context.filesDir, "remix_orders_export.csv")
        csvFile.printWriter().use { out ->
            out.println("orderId,customerId,date,totalAmount,totalCost,status,channel")
            // Retrieve all orders synchronously (blocking) for export simplicity
            val orders = repository.getAllOrdersBlocking()
            for (order in orders) {
                out.println(
                    "${order.id},${order.customerId},${order.date},${order.totalAmount},${order.totalCost},${order.status},${order.channel}"
                )
            }
        }
        return csvFile.absolutePath
    }
}

class StatisticsViewModelFactory(private val repository: RemixRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
