package com.example.jetttrivia.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetttrivia.components.Questions

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel(),
               modifier: Modifier
)
{
    Questions(viewModel = viewModel,
              modifier = modifier
    )
}