package com.example.imagefeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.imagefeed.ui.list.composable.ImageFeedHost
import com.example.imagefeed.ui.theme.ImageFeedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageFeedTheme {
                ImageFeedHost()
            }
        }
    }
}