package com.example.freevote.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    val navControllerInside = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(

                modifier = Modifier,
                drawerContainerColor = Color.White
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // Drawer menu items with click actions
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Profile",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {  },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Settings",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {  },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Terms & Conditions",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {  },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Privacy Policy",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {  },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "About",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Sign Out",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }

            }
        },
        drawerState = drawerState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Black)) { append("FREE") }
                                withStyle(style = SpanStyle(color = Color.Red)) { append("vote") }
                                withStyle(style = SpanStyle(color = Color(0xFF006400))) { append("!") }
                            },
                            fontSize = 48.sp,
                            fontFamily = FontFamily(Font(R.font.rubik_moonrocks))
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = { BottomNavigationBar(navControllerInside = navControllerInside, navController=navController) },
            content = { paddingValues ->
                NavigationContent(navController = navControllerInside, paddingValues = paddingValues)
            }
        )
    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu Item 1", modifier = Modifier.padding(8.dp))
        Text(text = "Menu Item 2", modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun NavigationContent(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(paddingValues) }
        composable("vote") { VoteScreen(paddingValues) }
        composable("statistics") { StatisticsScreen(paddingValues) }
        composable("about") { AboutScreen(paddingValues) }
    }
}

@Composable
fun BottomNavigationBar(navControllerInside: NavHostController, navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navControllerInside.navigate("home")}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ThumbUp, contentDescription = "Vote") },
            label = { Text("Vote") },
            selected = false,
            onClick = { navController.navigate("vote") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Statistics") },
            label = { Text("Statistics") },
            selected = false,
            onClick = { navControllerInside.navigate("statistics") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "About") },
            label = { Text("About") },
            selected = false,
            onClick = { navControllerInside.navigate("about") }
        )
    }
}

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    // Merging NewsHorizontalGallery into HomeScreen
    val viewModel: NewsViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val customFont = FontFamily(Font(R.font.rubix))
            Text(
                text = "News",
                fontFamily = customFont,
                color = Color.Red,
                fontSize = 36.sp,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            NewsHorizontalGallery(viewModel = viewModel)
        }
    }
}

@Composable
fun VoteScreen(paddingValues: PaddingValues) {
    // Content for Vote
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Vote Screen")
    }
}

@Composable
fun StatisticsScreen(paddingValues: PaddingValues) {
    // Content for Statistics
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Statistics Screen")
    }
}

@Composable
fun AboutScreen(paddingValues: PaddingValues) {
    // Content for About
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("About Screen")
    }
}


// The function that sets up the UI
@Composable
fun MyApp() {
    val viewModel: NewsViewModel = viewModel()

    // Fetch news when the app starts
    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    NewsHorizontalGallery(viewModel = viewModel) // Showing horizontally sliding gallery
}

// Data Classes
data class NewsResponse(val articles: List<Article>)
data class Article(
    val title: String,
    val urlToImage: String?,
    val description: String,
    val url: String // URL to open in browser
)

// Retrofit Service
interface NewsService {
    @GET("v2/top-headlines")
    suspend fun getPoliticalNews(
        @Query("category") category: String = "politics",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}

object RetrofitInstance {
    val api: NewsService by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)
    }
}

// ViewModel and Repository
class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    val newsLiveData = repository.newsLiveData

    fun fetchNews() {
        viewModelScope.launch {
            repository.fetchPoliticalNews()
        }
    }
}

class NewsRepository {
    val newsLiveData = MutableLiveData<List<Article>>()

    suspend fun fetchPoliticalNews() {
        val response = RetrofitInstance.api.getPoliticalNews(apiKey = "0895bd291a4c437b8bfbb6c145dabd65")
        newsLiveData.postValue(response.articles.take(5)) // Restrict to 5 articles
    }
}

// Compose UI for displaying horizontally sliding gallery
@Composable
fun NewsHorizontalGallery(viewModel: NewsViewModel) {
    val articles by viewModel.newsLiveData.observeAsState(emptyList())
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, shape = RoundedCornerShape(12.dp))
        .background(Color.White)) {


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), // Adjust height for slideshow feel
            contentPadding = PaddingValues(horizontal = 16.dp), // Padding for the edges
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {



            items(articles) { article ->
                NewsItemCardFancy(
                    article = article,
                    onClick = {
                        // Open the news article URL in a browser when clicked
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }}

@Composable
fun NewsItemCardFancy(article: Article, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(300.dp) // Larger width for a slideshow feel
            .height(300.dp) // Uniform height
            .clip(RoundedCornerShape(16.dp)) // Rounded corners
            .clickable { onClick() }
            .shadow(8.dp) // Shadow for depth effect
    ) {
        // Background Image with Parallax Effect
        article.urlToImage?.let { url ->
            Image(
                painter = rememberImagePainter(url), // Load image
                contentDescription = null,
                contentScale = ContentScale.Crop, // Crop the image
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Parallax effect on scroll
                        translationX = 0.8f * (300.dp.toPx() / 2f)
                    }
            )

            // Overlay for gradient effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 100f // Control the gradient overlay
                        )
                    )
            )

            // Text and source over the gradient
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp) // Padding within the box for text
            ) {
                Text(
                    text = article.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.title, // Assuming source is a string,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


// Preview function to see the UI in Android Studio's preview window

    @Composable
    fun PreviewMyApp() {
        // Dummy data for preview
        val dummyViewModel = NewsViewModel().apply {
            newsLiveData.value = listOf(
                Article("Preview Title 1", null, "Preview Description 1", "https://example.com"),
                Article("Preview Title 2", null, "Preview Description 2", "https://example.com"),
                Article("Preview Title 3", null, "Preview Description 3", "https://example.com"),
                Article("Preview Title 4", null, "Preview Description 4", "https://example.com"),
                Article("Preview Title 5", null, "Preview Description 5", "https://example.com")
            )
        }
        NewsHorizontalGallery(viewModel = dummyViewModel)
    }}