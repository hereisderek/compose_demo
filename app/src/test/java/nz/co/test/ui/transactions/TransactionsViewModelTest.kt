package nz.co.test.ui.transactions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import nz.co.test.MainCoroutineRule
import nz.co.test.transactions.data.models.Result
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.util.AppDispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TransactionsViewModelTest {

    private lateinit var repo: FakeTransactionRepository
    private lateinit var viewModel: TransactionsViewModel

    private val testDispatchers = UnconfinedTestDispatcher().let {
        AppDispatchers(it, it, it)
    }

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    public fun setUp() {
        repo = FakeTransactionRepository()
        viewModel = TransactionsViewModel(SavedStateHandle(), repo, testDispatchers)
    }


    private fun dummyTransaction(id: Int = 1) = Transaction(
        id, "2021-08-01T06:26:54", "Lockman-Anderson", 15.0, 23.4
    )

    private fun dummyTransactionList(size: Int = 5) = (1 .. 5).map {
        dummyTransaction(it)
    }


    @Test
    fun `WHEN repo returns empty result THEN viewModel shows empty` () = runTest {
        val results = mutableListOf<TransactionState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }
        val actualResult = results[0] as TransactionState.Transactions
        assertTrue(actualResult.transactions.isEmpty())
        job.cancel()
    }

    @Test
    fun `WHEN repo returns transaction list THEN viewModel shows the right list` () = runTest {
        val actualResult : List<Transaction> = dummyTransactionList(10)
        repo.injectTransactionsResult(Result.of(actualResult))

        val results = mutableListOf<TransactionState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }
        viewModel.refresh()

        assertEquals(
            actualResult, (results[1] as TransactionState.Transactions).transactions
        )
        job.cancel()
    }
    
    @Test
    fun `WHEN repo returns transaction list AND used clicked on one of the transaction THEN viewModel shows the right list with the selected transaction in a dialog` () = runTest {
        val expectedList : List<Transaction> = dummyTransactionList(10)
        val selected = expectedList[3]
        repo.injectTransactionsResult(Result.of(expectedList))

        val results = mutableListOf<TransactionState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }

        viewModel.refresh()
        viewModel.onTransactionSelected(selected)


        val actualResult = results[2] as TransactionState.Transactions
        assertEquals(expectedList, actualResult.transactions)
        assertEquals(selected, actualResult.focus)

        job.cancel()
    }

    @Test
    fun `WHEN repo returns transaction list AND used clicked on one of the transaction AND deselect it THEN viewModel shows the right list without the selected transaction in a dialog` () = runTest {
        val expectedList : List<Transaction> = dummyTransactionList(10)
        val selected = expectedList[3]
        repo.injectTransactionsResult(Result.of(expectedList))

        val results = mutableListOf<TransactionState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }

        viewModel.refresh()
        viewModel.onTransactionSelected(selected)
        viewModel.onTransactionDeselected()

        val actualResult = results[3] as TransactionState.Transactions
        assertEquals(expectedList, actualResult.transactions)
        assertEquals(null, actualResult.focus)

        job.cancel()
    }
}