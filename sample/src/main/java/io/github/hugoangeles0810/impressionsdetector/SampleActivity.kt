package io.github.hugoangeles0810.impressionsdetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.hugoangeles0810.impresssionsdetector.ImpressionDetector
import io.github.hugoangeles0810.impresssionsdetector.LocalImpressionDetector
import io.github.hugoangeles0810.impresssionsdetector.impression

class SampleActivity : ComponentActivity() {


    private val impressionDetector by lazy { ImpressionDetector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = (0..100).map { Item(id = it.toString(), name = "Item $it") }

        setContent {
            Surface {
                CompositionLocalProvider(
                    LocalImpressionDetector provides impressionDetector
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = items) {
                            ItemCard(
                                modifier = Modifier.impression(it.id) {
                                    println("Item ${it.id} was shown")
                                },
                                item = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    item: Item
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(item.id, style = MaterialTheme.typography.displayMedium)
            Text(item.name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}