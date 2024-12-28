package com.example.jetttrivia.repository

import android.util.Log
import com.example.jetttrivia.data.DataOrException
import com.example.jetttrivia.model.QuestionItem
import com.example.jetttrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val  api: QuestionApi)
{
    private val dataOrException = DataOrException<
            ArrayList<QuestionItem>,
            Boolean ,
            Exception
            >()
    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception>
    {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestion()
            if (dataOrException.data.toString().isNotEmpty())
                dataOrException.loading = false
        }
        catch (exception: Exception)
        {
            dataOrException.e= exception
            Log.d("Exc", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }

}