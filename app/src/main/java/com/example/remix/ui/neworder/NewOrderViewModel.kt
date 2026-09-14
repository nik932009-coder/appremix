package com.example.remix.ui.neworder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remix.data.CustomerEntity
import com.example.remix.data.ProductEntity
import com.example.remix.data.RemixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class OrderItemState(
    val product: ProductEntity,
    val quantity: Int
)

class NewOrderViewModel(private val repository: RemixRepository) : ViewModel() {
    val customers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomer: StateFlow<CustomerEntity?> = _selectedCustomer

    private val _orderItems = MutableStateFlow<List<OrderItemState>>(emptyList())
    val orderItems: StateFlow<List<OrderItemState>> = _orderItems

    fun selectCustomer(customer: CustomerEntity) {
        _selectedCustomer.value = customer
    }

    fun addProduct(product: ProductEntity, quantity: Int) {
        _orderItems.update { currentList ->
            val existingItem = currentList.find { it.product.sku == product.sku }
            if (existingItem != null) {
                currentList.map {
                    if (it.product.sku == product.sku) it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else {
                currentList + OrderItemState(product, quantity)
            }
        }
    }
    
    fun removeProduct(sku: String) {
        _orderItems.update { currentList -> currentList.filter { it.product.sku != sku } }
    }

    fun calculateTotal(): Double {
        return _orderItems.value.sumOf { it.product.realCatalogPrice * it.quantity }
    }

    fun confirmOrder(status: String, channel: String = "PEDIDOS") {
        val customer = _selectedCustomer.value ?: return
        val items = _orderItems.value
        if (items.isEmpty()) return

        val orderId = java.util.UUID.randomUUID().toString()
        val totalAmount = calculateTotal()
        val totalCost = items.sumOf { it.product.cost * it.quantity }
        val date = System.currentTimeMillis()

        val order = com.example.remix.data.OrderEntity(orderId, customer.id, date, totalAmount, totalCost, status, channel)
        val orderItemEntities = items.map { item ->
            com.example.remix.data.OrderItemEntity(
                id = java.util.UUID.randomUUID().toString(),
                orderId = orderId,
                sku = item.product.sku,
                quantity = item.quantity,
                unitPrice = item.product.realCatalogPrice,
                unitCost = item.product.cost
            )
        }

        viewModelScope.launch {
            repository.saveOrder(order, orderItemEntities)
            _selectedCustomer.value = null
            _orderItems.value = emptyList()
        }
    }
}

class NewOrderViewModelFactory(private val repository: RemixRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewOrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
