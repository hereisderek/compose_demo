package nz.co.test.transactions.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import logcat.logcat
import nz.co.test.transactions.data.models.Result
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.transactions.data.services.TransactionsService
import nz.co.test.util.AppDispatchers
import javax.inject.Inject


interface TransactionsRepository {
    suspend fun getTransactions(force: Boolean = false) : Result<List<Transaction>>
}

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsService: TransactionsService,
    private val dispatchers: AppDispatchers
) : TransactionsRepository {

    private val localTransactionsCache = MutableStateFlow<List<Transaction>>(emptyList())

    override suspend fun getTransactions(force: Boolean): Result<List<Transaction>> {
        val cache = localTransactionsCache.value
        return if (!force && cache.isNotEmpty()) {
            Result.of(cache)
        } else withContext(dispatchers.IO){
            try {
                transactionsService.retrieveTransactions().let {
                    logcat {
                        "received transactions from api, size:${it.size}"
                    }
                    localTransactionsCache.emit(it)
                    Result.of(it)
                }
            } catch (e: Exception) {
                Result.error(e)
            }
        }
    }
}