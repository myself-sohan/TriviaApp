package com.example.jetttrivia.components

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.jetttrivia.screens.QuestionsViewModel

@Composable
fun Questions(viewModel: QuestionsViewModel,
              modifier: Modifier
)
{
    val questions = viewModel.data.value.data?.toMutableList()
    val allAnswers = remember { mutableStateListOf<Int?>() }
    questions?.forEach {  allAnswers.add(null) }
    val questionIndex = remember {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current
    if(viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    }
    else
    {
        val question = try {
            questions?.get(questionIndex.intValue)
        } catch (ex: Exception) {
            null
        }
        if(questions != null) {
            QuestionDisplay(question = question!!,
                modifier = modifier,
                questionIndex = questionIndex,
                viewModel = viewModel,
                allAnswers = allAnswers,
                onNextClicked = {
                    questionIndex.intValue = questionIndex.intValue + 1
                },
                onPrevClicked = {
                    if(questionIndex.intValue <= 0)
                    {
                        questionIndex.intValue = 0
                        Toast.makeText(context, "Beginning of Quiz", Toast.LENGTH_SHORT).show()
                    }
                    else
                    questionIndex.intValue--
                }
            )
        }
    }
}