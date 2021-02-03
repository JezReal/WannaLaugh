package io.github.jezreal.wannalaugh.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jezreal.wannalaugh.models.Joke
import io.github.jezreal.wannalaugh.repository.JokeRepository
import io.github.jezreal.wannalaugh.wrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val repository: JokeRepository
) : ViewModel() {

    private val _joke = MutableLiveData<JokeState>(JokeState.Empty)
    val joke: LiveData<JokeState> = _joke

    private val _jokeEvent = Channel<JokeEvent>()
    val jokeEvent = _jokeEvent.receiveAsFlow()

    private val _currentJoke = MutableLiveData<Joke>()
    val currentJoke: LiveData<Joke> = _currentJoke

    sealed class JokeEvent {
        object NavigateToPunchlineFragment : JokeEvent()
    }

    sealed class JokeState {
        object Empty : JokeState()
        object Loading : JokeState()
        class Success(val joke: Joke) : JokeState()
        class Failure(val message: String) : JokeState()
    }

    fun loadJoke() {
        _joke.value = JokeState.Loading
        Log.d("JokeViewModel", "State: Loading")

        viewModelScope.launch(Dispatchers.Default) {
            when (val jokeResponse = repository.getRandomJoke()) {
                is Result.Success -> {
                    _joke.postValue(JokeState.Success(jokeResponse.data!!))
                    _currentJoke.postValue(jokeResponse.data)
                }
                is Result.Error -> {
                    _joke.postValue(JokeState.Failure(jokeResponse.message!!))
                }
            }
        }
    }

    fun loadPunchLine() {
        viewModelScope.launch {
            _jokeEvent.send(JokeEvent.NavigateToPunchlineFragment)
        }
    }
}