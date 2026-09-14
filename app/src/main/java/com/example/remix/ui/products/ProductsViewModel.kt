package com.example.remix.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remix.data.ProductEntity
import com.example.remix.data.RemixRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: RemixRepository) : ViewModel() {
    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProduct(sku: String, name: String, presentation: String, presentationGrams: Int, price: Double, cost: Double, margin: Double, stock: Int) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductEntity(sku, name, presentation, presentationGrams, price, cost, margin, stock)
            )
        }
    }
}

class ProductsViewModelFactory(private val repository: RemixRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
