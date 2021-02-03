package io.github.jezreal.wannalaugh.network

import io.github.jezreal.wannalaugh.models.Joke
import retrofit2.Response
import retrofit2.http.GET

interface JokeApi {

    @GET("random_joke")
    suspend fun getJoke(): Response<Joke>
}