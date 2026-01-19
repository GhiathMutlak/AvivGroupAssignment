package com.perfecta.avivgroupassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.perfecta.avivgroupassignment.presentation.navigation.NavigationRoot
import com.perfecta.avivgroupassignment.ui.theme.AvivGroupAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvivGroupAssignmentTheme {
                NavigationRoot()
            }
        }
    }
}