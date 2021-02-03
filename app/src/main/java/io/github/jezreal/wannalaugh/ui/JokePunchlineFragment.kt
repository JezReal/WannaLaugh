package io.github.jezreal.wannalaugh.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.jezreal.wannalaugh.databinding.FragmentJokePunchlineBinding
import io.github.jezreal.wannalaugh.ui.viewmodels.JokeViewModel

@AndroidEntryPoint
class JokePunchlineFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentJokePunchlineBinding
    private val viewModel: JokeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJokePunchlineBinding.inflate(inflater, container, false)

        viewModel.currentJoke.observe(viewLifecycleOwner, {
            binding.jokePunchline.text = it.punchline
        })


        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.loadJoke()
    }
}