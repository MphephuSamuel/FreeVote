import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.freevote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBot() {
    var isExpanded by remember { mutableStateOf(false) }

    // Main chat button
    FloatingActionButton(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier.padding(16.dp),
        containerColor = Color(0xFF6200EE) // Directly set the color
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chat),
            contentDescription = "Chat"
        )
    }

    // Expanded options if isExpanded is true
    if (isExpanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.End // Align to the end of the screen
        ) {
            // Option 1
            ChatOption("Change PIN") {
                // Handle option click
            }
            // Option 2
            ChatOption("View Notifications") {
                // Handle option click
            }
            // Option 3
            ChatOption("Send Request") {
                // Handle option click
            }
            // Option 4
            ChatOption("View Results") {
                // Handle option click
            }
        }
    }
}


@Composable
fun ChatOption(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1BEE7)) // Customize your color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 20.sp)
            Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null) // Arrow icon
        }
    }
}


@Preview
@Composable
fun PreviewChatBot() {
    ChatBot()
}
