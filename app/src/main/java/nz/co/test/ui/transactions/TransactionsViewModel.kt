package nz.co.test.ui.transactions

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.test.transactions.data.models.Response
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.transactions.data.repository.TransactionsRepository
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>(false)

    private val _transactions = transactionsRepository.transactions.map {
        it.map { it.sortedByDescending { it.transactionDate } }
    }.distinctUntilChanged()

    val transactions : LiveData<Response<List<Transaction>>> = _transactions

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _dataLoading.value = true
            transactionsRepository.refresh()
            _dataLoading.value = false
        }
    }
}