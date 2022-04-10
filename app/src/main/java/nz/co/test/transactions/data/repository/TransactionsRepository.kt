package nz.co.test.transactions.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.withContext
import nz.co.test.transactions.data.models.Response
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.transactions.data.services.TransactionsService
import nz.co.test.util.AppDispatchers
import javax.inject.Inject



interface TransactionsRepository {
    val transactions : LiveData<Response<List<Transaction>>>
    suspend fun refresh()
}

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsService: TransactionsService,
    private val dispatchers: AppDispatchers
) : TransactionsRepository {

    private val _transactions = MutableLiveData<Response<List<Transaction>>>()

    override val transactions: LiveData<Response<List<Transaction>>> get() = _transactions

    override suspend fun refresh() {
        _transactions.apply {
            value = Response.loading
            value = withContext(dispatchers.IO) {
                try {
                    Response.success(transactionsService.retrieveTransactions())
                } catch (e: Exception) {
                    Response.error(e)
                }
            }
        }
    }
}