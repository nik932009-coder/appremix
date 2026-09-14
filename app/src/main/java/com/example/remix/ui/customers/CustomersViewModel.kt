package com.example.remix.ui.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remix.data.CustomerEntity
import com.example.remix.data.RemixRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CustomersViewModel(private val repository: RemixRepository) : ViewModel() {
    val customers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCustomer(name: String, whatsapp: String, notes: String) {
        viewModelScope.launch {
            repository.insertCustomer(
                CustomerEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    whatsapp = whatsapp,
                    firstPurchaseDate = null,
                    lastPurchaseDate = null,
                    orderCount = 0,
                    totalSpent = 0.0,
                    notes = notes
                )
            )
        }
    }
}

class CustomersViewModelFactory(private val repository: RemixRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
