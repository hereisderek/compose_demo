package nz.co.test.transactions.data.services

import nz.co.test.transactions.data.models.Transaction
import retrofit2.http.GET

interface TransactionsService {
    @GET("test-data.json")
    suspend fun retrieveTransactions(): List<Transaction>
}

