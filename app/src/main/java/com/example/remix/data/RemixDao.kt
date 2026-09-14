package com.example.remix.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RemixDao {
    // Products
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    // Customers
    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    // Orders
    @Query("SELECT SUM(totalAmount) FROM orders")
    fun getTotalSales(): Flow<Double>

    @Query("SELECT SUM(totalCost) FROM orders")
    fun getTotalCost(): Flow<Double>

    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    // Retrieve all orders for export (blocking)
    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<OrderEntity>

    // Order Items
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsForOrder(orderId: String): Flow<List<OrderItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("UPDATE products SET stockUnits = stockUnits - :quantity WHERE sku = :sku")
    suspend fun decreaseStock(sku: String, quantity: Int)

}
