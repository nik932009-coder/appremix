package com.example.remix.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel) {
    // Collect values from the ViewModel
    val totalSales by viewModel.totalSales.collectAsState()
    val totalCost by viewModel.totalCost.collectAsState()
    val profit by viewModel.profit.collectAsState()
    val orderCount by viewModel.orderCount.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Estadísticas") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ventas Totales", style = MaterialTheme.typography.titleMedium)
                    Text("$${"%,.2f".format(totalSales)}", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Costo Total", style = MaterialTheme.typography.titleMedium)
                    Text("$${"%,.2f".format(totalCost)}", fontSize = 24.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ganancia", style = MaterialTheme.typography.titleMedium)
                    Text("$${"%,.2f".format(profit)}", fontSize = 24.sp, color = MaterialTheme.colorScheme.tertiary)
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pedidos Realizados", style = MaterialTheme.typography.titleMedium)
                    Text(orderCount.toString(), fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
            }
            // Export Button
            Button(
                onClick = {
                    val path = viewModel.exportOrdersCsv()
                    // Show a toast with the exported file path
                    android.widget.Toast.makeText(
                        androidx.compose.ui.platform.LocalContext.current,
                        "Exported to $path",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exportar pedidos a CSV")
            }
        }
    }
}
