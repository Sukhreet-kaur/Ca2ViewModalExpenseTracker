package com.example.ca2viewmodal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ca2viewmodal.ui.theme.Ca2ViewModalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ca2ViewModalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseSplitScreen()
                }
            }
        }
    }
}

@Composable
fun ExpenseSplitScreen(viewModel: ExpenseViewModel = viewModel()) {
    val context = LocalContext.current
    var nameInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var splitCountInput by remember { mutableStateOf("") }

    var showRecords by remember { mutableStateOf(false) }
    var recordsList by remember { mutableStateOf<List<ExpenseSplit>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Expense Splitter App",
            style = MaterialTheme.typography.headlineMedium
        )

        // Text field for Person Name
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Person Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Total Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = splitCountInput,
            onValueChange = { splitCountInput = it },
            label = { Text("Splitted Into (Number of persons)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
  Button(
            onClick = {
                val amount = amountInput.toDoubleOrNull() ?: 0.0
                val count = splitCountInput.toIntOrNull() ?: 1
                if (nameInput.isNotBlank() && amount > 0.0 && count > 0) {
                    viewModel.setData(nameInput, amount, count)
                    viewModel.calculateSplit()

                    Toast.makeText(context, "Split Calculated & Saved Successfully!", Toast.LENGTH_SHORT).show()
                    nameInput = ""
                    amountInput = ""
                    splitCountInput = ""
                    if (showRecords) {
                        recordsList = viewModel.getData()
                    }
                } else {
                    Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate & Save Split")
        }
        Button(
            onClick = {
                showRecords = true
                recordsList = viewModel.getData()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Show All Record")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display records in Column with Cards
        if (showRecords) {
            Text(
                text = "All Expense Records",
                style = MaterialTheme.typography.titleMedium
            )
            ExpenseRecordList(records = recordsList)
        }
    }
}

@Composable
fun ExpenseRecordList(records: List<ExpenseSplit>) {
    if (records.isEmpty()) {
        Text(
            text = "No records found.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(records) { record ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Person: ${record.personName}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Total Amount: ₹${record.totalAmount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Splitted Into: ${record.splitCount} persons",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Splitted Amount: ₹${String.format(java.util.Locale.getDefault(), "%.2f", record.splittedAmount)} per person",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
