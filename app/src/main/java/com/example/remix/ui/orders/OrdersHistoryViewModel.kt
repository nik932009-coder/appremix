package com.example.remix.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remix.data.OrderEntity
import com.example.remix.data.RemixRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class OrderHistoryItem(
    val order: OrderEntity,
    val customerName: String
)

class OrdersHistoryViewModel(repository: RemixRepository) : ViewModel() {
    val ordersHistory: StateFlow<List<OrderHistoryItem>> = combine(
        repository.allOrders,
        repository.allCustomers
    ) { orders, customers ->
        orders.map { order ->
            val cName = customers.find { it.id == order.customerId }?.name ?: "Desconocido"
            OrderHistoryItem(order, cName)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class OrdersHistoryViewModelFactory(private val repository: RemixRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrdersHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
