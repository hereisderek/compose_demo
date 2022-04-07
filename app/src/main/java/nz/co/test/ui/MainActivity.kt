package nz.co.test.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat
import nz.co.test.theme.TimelyTheme
import nz.co.test.ui.browse.BrowseViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : BrowseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.transactions.observe(this){
            logcat { "transactions: $it" }
        }
        setContent {
            TimelyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android+")

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()

    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimelyTheme {
        Greeting("Android")
    }
}