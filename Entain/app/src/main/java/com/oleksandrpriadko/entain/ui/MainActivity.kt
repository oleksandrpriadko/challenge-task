package com.oleksandrpriadko.entain.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oleksandrpriadko.entain.ui.theme.EntainTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntainTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KoinContext {
                        // I did not use navigation framework because there is no task to navigate
                        // to race details screen
                        val viewModel: MainViewModel = koinViewModel()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        RaceList(
                            modifier = Modifier.padding(innerPadding),
                            uiState = uiState,
                            onCategorySelectionChanged = { raceCategory, isSelected ->
                                viewModel.changeCategorySelection(raceCategory, isSelected)
                            }
                        )
                    }
                }
            }
        }
    }
}