package com.example.jetttrivia.network

import com.example.jetttrivia.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET(value = "world.json")
    suspend fun getAllQuestion(): Question
}