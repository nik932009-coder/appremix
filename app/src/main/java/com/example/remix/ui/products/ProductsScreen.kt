package com.example.remix.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remix.data.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(viewModel: ProductsViewModel) {
    val products by viewModel.products.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos REMIX") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
            items(products) { product ->
                ProductItem(product)
            }
        }

        if (showAddDialog) {
            AddProductDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { sku, name, pres, grams, price, cost, margin, stock ->
                    viewModel.addProduct(sku, name, pres, grams, price, cost, margin, stock)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun ProductItem(product: ProductEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${product.name} - ${product.presentation}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "SKU: ${product.sku}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Precio Catálogo: $${product.realCatalogPrice}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Costo: $${product.cost} | Margen: ${String.format("%.1f", product.targetMargin)}%", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Stock Actual: ${product.stockUnits} un.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onAdd: (String, String, String, Int, Double, Double, Double, Int) -> Unit) {
    var sku by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var presentation by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU (ej: MIX-250)") })
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre (ej: Mix Tradicional)") })
                OutlinedTextField(value = presentation, onValueChange = { presentation = it }, label = { Text("Presentación (ej: 250g)") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio Real Catálogo") })
                OutlinedTextField(value = cost, onValueChange = { cost = it }, label = { Text("Costo") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = price.toDoubleOrNull() ?: 0.0
                val c = cost.toDoubleOrNull() ?: 0.0
                val margin = if (p > 0) ((p - c) / p) * 100 else 0.0
                onAdd(sku, name, presentation, 0, p, c, margin, 0)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
