package com.example.jetttrivia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetttrivia.model.Question
import com.example.jetttrivia.screens.QuestionsViewModel
import com.example.jetttrivia.screens.TriviaHome
import com.example.jetttrivia.ui.theme.JettTriviaTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JettTriviaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //val viewModel: QuestionsViewModel by viewModels()
                    TriviaHome(
                        modifier =  Modifier.padding(innerPadding),
                        //viewModel = viewModel
                    )
                }
            }
        }
    }
}





