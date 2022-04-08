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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import nz.co.test.transactions.data.models.Response
import nz.co.test.transactions.data.models.Transaction
import nz.co.test.util.twoDecimalString
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TransactionsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Transactions",
                        // color = LocalContentColor.current,
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                }
            )
        }
    ) {
        val viewModel = hiltViewModel<TransactionsViewModel>()
        val transactionsState = viewModel.transactions.observeAsState(Response.loading)
        when(val response = transactionsState.value) {
            is Response.Success -> TransactionsList(response.data)
            is Response.Loading -> FullScreenLoading()
            else -> Unit
        }

    }
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

@Preview
@Composable
private fun TransactionsList(
    transactions: List<Transaction> = (1 until 10).map { dummyTransaction(it) }
) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 18.dp)
    ) {
        items(transactions) {
            TransactionCell(it)
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun TransactionCell(
    transaction: Transaction = Transaction(
        1, "2021-08-01T06:26:54", "Lockman-Anderson", 15.0, 23.4
    )
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
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