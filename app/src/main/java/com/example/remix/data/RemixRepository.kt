package com.example.remix.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class RemixRepository(private val dao: RemixDao) {
    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()

    suspend fun insertProduct(product: ProductEntity) {
        dao.insertProduct(product)
    }

    val allCustomers: Flow<List<CustomerEntity>> = dao.getAllCustomers()

    val totalSales: Flow<Double> = dao.getTotalSales()
    val totalCost: Flow<Double> = dao.getTotalCost()
    val orderCount: Flow<Int> = dao.getOrderCount()

    suspend fun insertCustomer(customer: CustomerEntity) {
        dao.insertCustomer(customer)
    }

    suspend fun saveOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        dao.insertOrder(order)
        dao.insertOrderItems(items)
        if (order.status == "ENTREGADO") {
            items.forEach { item ->
                dao.decreaseStock(item.sku, item.quantity)
            }
        }
    }
    // Blocking retrieval of all orders for CSV export
    fun getAllOrdersBlocking(): List<OrderEntity> = kotlinx.coroutines.runBlocking {
        dao.getAllOrders()
    }

}
