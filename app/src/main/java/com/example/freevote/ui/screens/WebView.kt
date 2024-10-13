import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    // Using Box to center the WebView
    Box(
        modifier = Modifier.fillMaxSize(), // Fill the maximum size of the parent
        contentAlignment = Alignment.Center // Center content
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            // Allow the WebView to handle the URL loading
                            view?.loadUrl(request?.url.toString())
                            return true
                        }
                    }
                    settings.javaScriptEnabled = true // Enable JavaScript if needed
                    loadUrl(url) // Load the specified URL
                }
            },
            update = { webView ->
                webView.loadUrl(url) // Update the URL if needed
            },
            modifier = Modifier
                .size(width = 1000.dp, height = 1000.dp) // Set specific width and height for the WebView
        )
    }
}
