package Templates

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.Objects.TableColumn

data class TableColumnT<T>(
    val title: String,
    val weight: Float = 1f,
    val content: (T) -> String
)

@Composable
fun <T> TableGridT(
    items: List<T>,
    columns: List<TableColumn<T>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            columns.forEach { column ->
                GridHeader(
                    modifier = Modifier.weight(column.weight),
                    text = column.title
                )
            }
        }

        // Data rows
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                columns.forEach { column ->
                    GridCell(
                        modifier = Modifier.weight(column.weight),
                        text = column.content(item)
                    )
                }
            }
        }
    }
}

@Composable
private fun GridHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        AutoResizedTextT(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 2,
        )
    }
}

@Composable
private fun GridCell(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(0.5.dp, Color.LightGray)
            .padding(horizontal = 4.dp), // Added horizontal padding
        contentAlignment = Alignment.Center
    ) {
        AutoResizedTextT(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(), // Added fillMaxWidth
            textAlign = TextAlign.Center // Added center alignment
        )
    }
}

@Composable
fun AutoResizedTextT(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Center
) {
    var fontSize by remember(text) { mutableStateOf(style.fontSize) }
    var readyToDraw by remember { mutableStateOf(false) }
    
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult ->
            if (!readyToDraw) {
                if (textLayoutResult.hasVisualOverflow) {
                    fontSize *= 0.9f
                } else {
                    readyToDraw = true
                }
            }
        }
    )
}
