package io.github.jezreal.wannalaugh.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.jezreal.wannalaugh.R
import io.github.jezreal.wannalaugh.databinding.FragmentJokeSetupBinding
import io.github.jezreal.wannalaugh.ui.viewmodels.JokeViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class JokeSetupFragment : Fragment() {

    private lateinit var binding: FragmentJokeSetupBinding
    private val viewModel: JokeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJokeSetupBinding.inflate(inflater, container, false)

        viewModel.joke.observe(viewLifecycleOwner, { joke ->
            when (joke) {
                is JokeViewModel.JokeState.Loading -> {
                    binding.jokeSetup.visibility = View.GONE
                    binding.progressbar.visibility = View.VISIBLE
                }
                is JokeViewModel.JokeState.Success -> {
                    binding.jokeSetup.visibility = View.VISIBLE
                    binding.progressbar.visibility = View.GONE
                    binding.jokeSetup.text = joke.joke.setup
                }
                is JokeViewModel.JokeState.Failure -> {
                    binding.progressbar.visibility = View.GONE
                    binding.jokeSetup.visibility = View.VISIBLE
                    binding.jokeSetup.text = joke.message
                }
                is JokeViewModel.JokeState.Empty -> {
                    viewModel.loadJoke()
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.jokeEvent.collect {
                when (it) {
                    is JokeViewModel.JokeEvent.NavigateToPunchlineFragment -> {
                        findNavController().navigate(JokeSetupFragmentDirections.actionJokeSetupFragmentToJokePunchlineFragment())
                    }
                }
            }
        }

        binding.navigate.setOnClickListener {
            viewModel.loadPunchLine()
        }

        return binding.root
    }
}