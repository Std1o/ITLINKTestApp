package com.stdio.it_link_testapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.domain.model.ThumbnailData
import com.stdio.it_link_testapp.presentation.ui.components.CenteredColumn
import com.stdio.it_link_testapp.presentation.ui.theme.ITLINKTestAppTheme
import com.stdio.it_link_testapp.presentation.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITLINKTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<ImagesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val networkIsEnabled by viewModel.networkIsEnabled.collectAsState()
    if (uiState.isEmpty() && !networkIsEnabled) {
        CenteredColumn {
            Text("Ожидание сети...")
        }
    } else {
        LazyColumn {
            items(uiState) { item ->
                val imageState by item.collectAsState(LoadableData.Loading)
                when (imageState) {
                    is LoadableData.Error -> Text((imageState as LoadableData.Error).exception)
                    is LoadableData.Loading -> CircularProgressIndicator()
                    is LoadableData.Success -> {
                        val image = (imageState as LoadableData.Success<String>).data
                        AsyncImage(
                            model = image,
                            contentDescription = "Thumbnail",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    is ThumbnailData.Placeholder -> {
                        AsyncImage(
                            model = R.drawable.no_image,
                            contentDescription = "Thumbnail",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}