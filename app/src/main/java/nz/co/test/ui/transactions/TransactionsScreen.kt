package nz.co.test.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import logcat.logcat
import nz.co.test.transactions.data.models.Response
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.util.lifecycleAwareFlowAsState
import nz.co.test.util.rememberFlow
import nz.co.test.util.twoDecimalString
import org.intellij.lang.annotations.JdkConstants



data class TransactionListener(
    val onClick: ((transition: Transaction)->Unit),
    val onDismiss: (()->Unit)
)

private val TransactionsViewModel.transactionListener : TransactionListener get() =
    TransactionListener (this::onTransactionSelected, this::onTransactionDeselected)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TransactionAppBar() }
    ) {
        val transactionsState by viewModel.state.lifecycleAwareFlowAsState()
        val transactionListener = viewModel.transactionListener
        TransactionsScreenContent(transactionsState, transactionListener)
    }
}

@Preview
@Composable
private fun TransactionAppBar(title: String = "Transactions") {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.wrapContentWidth()
            )
        }
    )
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

private fun dummyTransaction(id: Int = 1) = Transaction(
    id, "2021-08-01T06:26:54", "Lockman-Anderson", 15.0, 23.4
)

@Composable
private fun TransactionsScreenContent(
    state: TransactionState,
    transactionListener: TransactionListener
) {
    when(state) {
        is TransactionState.Loading -> FullScreenLoading()
        is TransactionState.Transactions -> {
            val focus = state.focus
            TransactionsList(state.transactions, transactionListener)
            if (focus != null) {
                showTransactionDialog(focus) {
                    transactionListener.onDismiss.invoke()
                }
            }
        }
        else -> Unit
    }
}

@Preview
@Composable
private fun TransactionsList(
    transactions: List<Transaction> = (1 until 10).map { dummyTransaction(it) },
    transactionListener: TransactionListener? = null
) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 18.dp)
    ) {
        items(transactions) {
            TransactionCell(it) {
                transactionListener?.onClick?.invoke(it)
            }
        }
    }
}

@Preview
@Composable
private fun showTransactionDialog(
    transaction: Transaction = dummyTransaction(),
    onDismiss: (() -> Unit)? = null
) {
    AlertDialog(onDismissRequest = {
        onDismiss?.invoke()
    }, title = {
        Text(text = "Transaction", fontWeight = FontWeight.SemiBold)
    }, text = {
        Text(text = transaction.run {
            """
            Id:$id
            Date:$transactionDate
            Summary:$summary
            Debit:$debit
            Credit:$credit
        """.trimIndent()
        })
    }, confirmButton = {
        TextButton(onClick = {
            onDismiss?.invoke()
        }) {
            Text(text = "Confirm", fontWeight = FontWeight.SemiBold)
        }
    })
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun TransactionCell(
    transaction: Transaction = Transaction(
        1, "2021-08-01T06:26:54", "Lockman-Anderson", 15.0, 23.4
    ),
    onClick: (()->Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        onClick = {
            onClick?.invoke()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            ListItem(text = {
                Text(transaction.summary)
            }, secondaryText = {
                Text(transaction.transactionDate)
            }, singleLineSecondaryText = true, modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
            )

            transaction.apply {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val redColor = Color.Red
                    val greenColor = Color(0xFF228B22)

                    when{
                        (debit == 0.0 || credit == 0.0) -> {
                            Text(
                                (debit + credit).twoDecimalString,
                                color = if (debit == 0.0) greenColor else redColor,
                                textAlign = TextAlign.End
                            )
                        }
                        else -> Column(
                            modifier = Modifier.wrapContentWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                debit.twoDecimalString,
                                color = redColor,
                                textAlign = TextAlign.End
                            )
                            Text(
                                credit.twoDecimalString,
                                color = greenColor,
                                textAlign = TextAlign.End
                            )
                        }
                    }

                }
            }

        }
    }
}