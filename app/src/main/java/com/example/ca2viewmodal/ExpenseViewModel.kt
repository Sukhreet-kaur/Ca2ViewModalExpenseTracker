package com.example.ca2viewmodal

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
data class ExpenseSplit(
    val personName: String,
    val totalAmount: Double,
    val splitCount: Int,
    val splittedAmount: Double
)

class ExpenseViewModel : ViewModel() {
    private var personName: String = ""
    private var totalAmount: Double = 0.0
    private var splitCount: Int = 1
    private val _expenseRecords = mutableStateListOf<ExpenseSplit>()
    val expenseRecords: List<ExpenseSplit> get() = _expenseRecords
    fun setData(name: String, amount: Double, count: Int) {
        personName = name
        totalAmount = amount
        splitCount = if (count > 0) count else 1
    }
    fun calculateSplit() {
        if (personName.isBlank() || totalAmount <= 0.0) return
        val splittedAmount = totalAmount / splitCount
        val record = ExpenseSplit(
            personName = personName,
            totalAmount = totalAmount,
            splitCount = splitCount,
            splittedAmount = splittedAmount
        )
        _expenseRecords.add(record)
    }
    fun getData(): List<ExpenseSplit> {
        return _expenseRecords
    }
}
