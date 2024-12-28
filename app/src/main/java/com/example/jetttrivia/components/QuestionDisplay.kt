package com.example.jetttrivia.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetttrivia.utils.AppColors
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetttrivia.model.QuestionItem
import com.example.jetttrivia.screens.QuestionsViewModel


@Composable
fun QuestionDisplay(
    question: QuestionItem,
    modifier: Modifier,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    allAnswers : MutableList<Int?>,
    onPrevClicked: (Int) -> Unit = {},
    onNextClicked: (Int) -> Unit ={}

)
{
    val choicesState = remember(question){
        question.choices.toMutableList()
    }
    val answerState = remember (question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question)
    {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer : (Int) -> Unit = remember(question)
    {
        {
            correctAnswerState.value = choicesState[it] == question.answer
            answerState.value = it

        }
    }
    val score = remember()
    {
        mutableIntStateOf(0)
    }
    var updateScore : (Boolean?, Int) -> Unit =
    {
        isCorrect, choice ->
        val previousAnswer = allAnswers[questionIndex.value] // Previous answer index (if any)
        val previousAnswerWasCorrect = previousAnswer != null && choicesState[previousAnswer] == question.answer

        when {
            // Case 1: First-time answer
            previousAnswer == null -> {
                if (isCorrect == true) score.intValue += 1
                // No change for incorrect answer on first attempt
            }

            // Case 2: Changing from Incorrect → Correct
            isCorrect == true && !previousAnswerWasCorrect -> {
                score.intValue += 1
            }

            // Case 3: Changing from Correct → Incorrect
            isCorrect == false && previousAnswerWasCorrect -> {
                score.intValue -= 1
            }

            // Case 4: No change (same answer selected again)
            else -> {
                // No action needed
            }
        }

        // Update the recorded answer
        allAnswers[questionIndex.value] = choice
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f),0f)
    Surface(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = AppColors.mDarkPurple
    )
    {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        )
        {
            if(questionIndex.value>=1)
            {
                ShowProgressScore(score = score.intValue)
            }
            QuestionTracker(
                counter = questionIndex.value+1,
                outOf = viewModel.data.value.data?.size!!
            )
            DrawDottedLines(pathEffect)
            Column()
            {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    lineHeight = 22.sp
                )
                choicesState.forEachIndexed { index, answerText ->
                    Row(modifier = Modifier
                        .padding(3.dp)
                        .height(45.dp)
                        .fillMaxWidth()
                        .border(
                            width = 4.dp, brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColors.mOffDarkPurple,
                                    AppColors.mOffDarkPurple
                                )
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomStartPercent = 50,
                                bottomEndPercent = 50
                            )
                        )
                        .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        RadioButton(selected = (answerState.value==index) || (allAnswers[questionIndex.value] == index),
                            onClick = {
                                updateAnswer(index)
                                updateScore(correctAnswerState.value, index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors
                                (
                                    selectedColor = if((correctAnswerState.value == true && index == answerState.value)
                                                || (allAnswers[questionIndex.value] == index && choicesState[index] == question.answer))
                                                    {
                                                        Color.Green.copy(alpha = 0.2f)
                                                    }
                                                    else
                                                    {
                                                        Color.Red.copy(alpha = 0.2f)

                                                    }
                                )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                color = if ((correctAnswerState.value == true && index == answerState.value) ||
                                    (allAnswers[questionIndex.value] == index && choicesState[index] == question.answer))
                                {
                                    Color.Green
                                } else if ((correctAnswerState.value == false && index == answerState.value) ||
                                    (allAnswers[questionIndex.value] == index && choicesState[index] != question.answer))
                                {
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                },
                                fontSize = 17.sp)){

                                append(answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
                Row(modifier= Modifier.padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { onPrevClicked(questionIndex.value) },
                        modifier = Modifier
                            .padding(3.dp)
                            .weight(2f),
                        shape = RoundedCornerShape(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.mLightBlue)
                    )
                    {
                        Text(
                            text = "Previous",
                            modifier = Modifier.padding(4.dp),
                            color = AppColors.mOffWhite,
                            fontSize = 17.sp
                        )
                    }
                    Spacer(modifier= Modifier.weight(1f))
                    Button(
                        onClick = { onNextClicked(questionIndex.value) },
                        modifier = Modifier
                            .padding(3.dp)
                            .weight(2f),
                        shape = RoundedCornerShape(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.mLightBlue)
                    )
                    {
                        Text(
                            text = "Next",
                            modifier = Modifier.padding(4.dp),
                            color = AppColors.mOffWhite,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun DrawDottedLines(pathEffect: PathEffect)
{
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    {
        drawLine(color = AppColors.mLightGray,
                start = Offset(0f,0f),
                end = Offset(size.width,0f),
                pathEffect = pathEffect
            )
    }
}
@Composable
fun QuestionTracker(counter : Int =10,
                    outOf : Int = 100)
{
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None))
        {
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp)
                )
            {
                append("Question $counter/")
            }
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp))
            {
                append("$outOf")
            }
        }
    },
        modifier = Modifier.padding(20.dp)
    )
}

@Preview
@Composable
fun ShowProgressScore(score: Int = 5)
{
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),
        Color(0xFFBE6BE5)))
    val progressFactor = remember(score)
    {
        mutableFloatStateOf(score * 0.01f)//0.000205f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(3.dp)
            .border(width = 4.dp,
                brush = Brush.linearGradient(colors = listOf(AppColors.mLightPurple , AppColors.mLightPurple)),
                shape = RoundedCornerShape(34.dp))
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    bottomStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50
                  )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor.floatValue)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )
        {
            Text(text = score.toString(),
                color = AppColors.mOffWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(2.dp),
                textAlign = TextAlign.Center
            )

        }
    }
}
