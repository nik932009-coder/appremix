package com.example.remix.ui.neworder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remix.data.CustomerEntity
import com.example.remix.data.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(viewModel: NewOrderViewModel) {
    val customers by viewModel.customers.collectAsState()
    val products by viewModel.products.collectAsState()
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val orderItems by viewModel.orderItems.collectAsState()

    var showCustomerDialog by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nuevo Pedido REMIX") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            
            // Sección Cliente
            Text("Cliente:", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = { showCustomerDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedCustomer?.name ?: "Seleccionar Cliente")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Productos
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Productos:", style = MaterialTheme.typography.titleMedium)
                Button(onClick = { showProductDialog = true }) {
                    Text("+ Agregar")
                }
            }

            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(orderItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("${item.product.name} - ${item.product.presentation}")
                                Text("${item.quantity} x $${item.product.realCatalogPrice}")
                            }
                            IconButton(onClick = { viewModel.removeProduct(item.product.sku) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Totales
            Text(
                text = "TOTAL: $${viewModel.calculateTotal()}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            val context = androidx.compose.ui.platform.LocalContext.current

            Button(
                onClick = {
                    val customerName = selectedCustomer?.name ?: ""
                    val itemsText = orderItems.joinToString("\n") { 
                        "• ${it.product.name} ${it.product.presentation} — $${it.product.realCatalogPrice * it.quantity}" 
                    }
                    val totalText = viewModel.calculateTotal()

                    val message = """
                        REMIX 🌿
                        
                        Hola $customerName!
                        
                        Te paso tu pedido:
                        
                        $itemsText
                        
                        TOTAL: $$totalText
                    """.trimIndent()

                    val sendIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, message)
                        type = "text/plain"
                    }
                    context.startActivity(android.content.Intent.createChooser(sendIntent, null))
                }, 
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCustomer != null && orderItems.isNotEmpty()
            ) {
                Text("Generar Presupuesto (WhatsApp)")
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.confirmOrder("CONFIRMADO") },
                    modifier = Modifier.weight(1f),
                    enabled = selectedCustomer != null && orderItems.isNotEmpty()
                ) {
                    Text("Confirmar")
                }
                
                Button(
                    onClick = { viewModel.confirmOrder("ENTREGADO") },
                    modifier = Modifier.weight(1f),
                    enabled = selectedCustomer != null && orderItems.isNotEmpty()
                ) {
                    Text("Entregar")
                }
            }
        }

        if (showCustomerDialog) {
            AlertDialog(
                onDismissRequest = { showCustomerDialog = false },
                title = { Text("Seleccionar Cliente") },
                text = {
                    LazyColumn {
                        items(customers) { c ->
                            TextButton(
                                onClick = {
                                    viewModel.selectCustomer(c)
                                    showCustomerDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(c.name)
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showCustomerDialog = false }) { Text("Cerrar") } }
            )
        }

        if (showProductDialog) {
            var qty by remember { mutableStateOf("1") }
            var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
            
            AlertDialog(
                onDismissRequest = { showProductDialog = false },
                title = { Text(if (selectedProduct == null) "Elegir Producto" else "Cantidad") },
                text = {
                    Column {
                        if (selectedProduct == null) {
                            LazyColumn {
                                items(products) { p ->
                                    TextButton(
                                        onClick = { selectedProduct = p },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("${p.name} - $${p.realCatalogPrice}")
                                    }
                                }
                            }
                        } else {
                            Text("Producto: ${selectedProduct!!.name}")
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Cantidad") })
                        }
                    }
                },
                confirmButton = {
                    if (selectedProduct != null) {
                        Button(onClick = {
                            val q = qty.toIntOrNull() ?: 1
                            viewModel.addProduct(selectedProduct!!, q)
                            showProductDialog = false
                        }) { Text("Agregar al pedido") }
                    }
                },
                dismissButton = { 
                    TextButton(onClick = { 
                        if (selectedProduct != null) {
                            selectedProduct = null // Back to list
                        } else {
                            showProductDialog = false 
                        }
                    }) { Text(if (selectedProduct != null) "Atrás" else "Cancelar") } 
                }
            )
        }
    }
}
