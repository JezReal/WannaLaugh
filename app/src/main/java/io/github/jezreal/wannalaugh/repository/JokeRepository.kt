package io.github.jezreal.wannalaugh.repository

import io.github.jezreal.wannalaugh.models.Joke
import io.github.jezreal.wannalaugh.network.JokeApi
import io.github.jezreal.wannalaugh.wrappers.Result
import java.lang.Exception
import javax.inject.Inject

class JokeRepository @Inject constructor(
    private val api: JokeApi
) {

    suspend fun getRandomJoke(): Result<Joke> {
        return try {
            val response = api.getJoke()
            val result = response.body()

            if (response.isSuccessful && result != null) {
                Result.Success(result)
            } else {
                Result.Error(response.message())
            }
        } catch (e: Exception) {
            Result.Error(e.stackTraceToString())
        }
    }
}