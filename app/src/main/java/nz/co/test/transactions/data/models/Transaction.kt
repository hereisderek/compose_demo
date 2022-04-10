package nz.co.test.transactions.data.models

data class Transaction(
    val id: Int,
    val transactionDate: String,
    val summary: String,
    val debit: Double,
    val credit: Double
)