package io.github.jezreal.wannalaugh.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jezreal.wannalaugh.network.JokeApi
import io.github.jezreal.wannalaugh.repository.JokeRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://official-joke-api.appspot.com/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideJokeApi(): JokeApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(JokeApi::class.java)

    @Singleton
    @Provides
    fun provideJokeRepository(api: JokeApi): JokeRepository = JokeRepository(api)
}