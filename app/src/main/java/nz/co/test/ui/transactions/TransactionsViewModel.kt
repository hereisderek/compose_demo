package nz.co.test.ui.transactions

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.logcat
import nz.co.test.transactions.data.models.Response
import nz.co.test.transactions.data.models.Result
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.transactions.data.repository.TransactionsRepository
import nz.co.test.util.AppDispatchers
import nz.co.test.util.threadName
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transactionsRepository: TransactionsRepository,
    private val dispatchers: AppDispatchers
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionState>(TransactionState.Loading)
    private val loading = MutableStateFlow(false)
    private val focusedTransaction = MutableStateFlow<Transaction?>(null)
    private val transactions : MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())

    val state: StateFlow<TransactionState> get() = _state


    init {
        viewModelScope.launch(dispatchers.Default) {
            combine(loading, transactions, focusedTransaction) { loading, transactions, focus ->
                logcat { "combine Running on thread:$threadName" }
                if (loading) TransactionState.Loading else {
                    TransactionState.Transactions(transactions, focus)
                }
            }.catch {
                TransactionState.Error(it)
            }.collect {
                _state.value = it
            }
        }

        refresh()
    }

    fun refresh(force: Boolean = false) {
        viewModelScope.launch(dispatchers.Default) {
            runCatching {
                loading.value = true
                val data = when(val result = transactionsRepository.getTransactions(force)) {
                    is Result.Success -> result.data
                    is Result.Error -> throw result.exception
                }
                transactions.emit(data)
            }
            loading.value = false
        }
    }

    fun onTransactionSelected(transaction: Transaction) {
        logcat { "onTransactionSelected: $transaction" }
        viewModelScope.launch {
            focusedTransaction.value = transaction
        }
    }


    fun onTransactionDeselected() {
        viewModelScope.launch {
            focusedTransaction.value = null
        }
    }
}


sealed class TransactionState {
    object Loading : TransactionState()

    data class Transactions(
        val transactions: List<Transaction> = emptyList(),
        val focus: Transaction? = null
        ) : TransactionState()

    data class Error(val error: Throwable) : TransactionState()
}