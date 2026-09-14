package com.example.remix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.remix.theme.RemixTheme
import com.example.remix.ui.products.ProductsScreen
import com.example.remix.ui.products.ProductsViewModel
import com.example.remix.ui.products.ProductsViewModelFactory
import com.example.remix.ui.customers.CustomersScreen
import com.example.remix.ui.customers.CustomersViewModel
import com.example.remix.ui.customers.CustomersViewModelFactory
import com.example.remix.ui.neworder.NewOrderScreen
import com.example.remix.ui.neworder.NewOrderViewModel
import com.example.remix.ui.neworder.NewOrderViewModelFactory
import com.example.remix.ui.orders.OrdersHistoryScreen
import com.example.remix.ui.orders.OrdersHistoryViewModel
import com.example.remix.ui.orders.OrdersHistoryViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val repository = (application as RemixApplication).repository
    val productsViewModel: ProductsViewModel by viewModels { ProductsViewModelFactory(repository) }
    val customersViewModel: CustomersViewModel by viewModels { CustomersViewModelFactory(repository) }
    val newOrderViewModel: NewOrderViewModel by viewModels { NewOrderViewModelFactory(repository) }
    val ordersHistoryViewModel: OrdersHistoryViewModel by viewModels { OrdersHistoryViewModelFactory(repository) }

    enableEdgeToEdge()
    setContent {
      RemixTheme { 
        var currentScreen by remember { mutableStateOf("Pedido") }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AddShoppingCart, contentDescription = "Pedido") },
                        label = { Text("Pedido") },
                        selected = currentScreen == "Pedido",
                        onClick = { currentScreen = "Pedido" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                        label = { Text("Historial") },
                        selected = currentScreen == "Historial",
                        onClick = { currentScreen = "Historial" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = "Productos") },
                        label = { Text("Productos") },
                        selected = currentScreen == "Productos",
                        onClick = { currentScreen = "Productos" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Clientes") },
                        label = { Text("Clientes") },
                        selected = currentScreen == "Clientes",
                        onClick = { currentScreen = "Clientes" }
                    )
                }
            }
        ) { padding ->
            Surface(modifier = Modifier.fillMaxSize().padding(padding), color = MaterialTheme.colorScheme.background) { 
                when (currentScreen) {
                    "Pedido" -> NewOrderScreen(newOrderViewModel)
                    "Historial" -> OrdersHistoryScreen(ordersHistoryViewModel)
                    "Productos" -> ProductsScreen(productsViewModel)
                    "Clientes" -> CustomersScreen(customersViewModel)
                }
            } 
        }
      }
    }
  }
}
