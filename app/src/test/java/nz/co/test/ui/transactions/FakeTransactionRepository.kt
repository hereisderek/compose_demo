package nz.co.test.ui.transactions

import androidx.annotation.VisibleForTesting
import nz.co.test.transactions.data.models.Result
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.transactions.data.repository.TransactionsRepository
import javax.inject.Inject

class FakeTransactionRepository @Inject constructor() : TransactionsRepository {


    private var injectedTransactionsResult : Result<List<Transaction>> = Result.of(emptyList()); get() = synchronized(this){
        field
    }

    @VisibleForTesting
    fun injectTransactionsResult(result: Result<List<Transaction>>) {
        this.injectedTransactionsResult = result
    }

    override suspend fun getTransactions(force: Boolean): Result<List<Transaction>>{
        return injectedTransactionsResult
    }

}