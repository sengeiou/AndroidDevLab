package com.example.composelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen() {
        Column {
            Text(
                text = "hello world!!",
                fontSize = 16.sp
            )
        }
    }

}