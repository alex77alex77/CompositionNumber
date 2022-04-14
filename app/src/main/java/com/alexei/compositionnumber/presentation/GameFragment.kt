package com.alexei.compositionnumber.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexei.compositionnumber.R
import com.alexei.compositionnumber.databinding.FragmentGameBinding
import com.alexei.compositionnumber.databinding.FragmentGameFinishedBinding
import com.alexei.compositionnumber.domain.entity.GameResult
import com.alexei.compositionnumber.domain.entity.GameSettings
import com.alexei.compositionnumber.domain.entity.Level


class GameFragment : Fragment() {
    private val viewModelFactory by lazy {
        GameViewModelFactory(level,requireActivity().application)
    }
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }
    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {

        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            for (i in 0 until tvOptions.size) {
                tvOptions[i].text = it.optionsList[i].toString()
            }
        }


        viewModel.percentOfRightAnswer.observe(viewLifecycleOwner) {

            binding.progressBar.progress = it
        }


        viewModel.enoughCountOfRightAnswer.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.setTextColor(getColorByState(it))
        }


        viewModel.enoughPercentOfRightAnswer.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }


        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }


        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }


        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }


        viewModel.textProgressAnswer.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = it
        }

    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }


    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private fun launchGameFinishedFragment(result: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, GameFinishedFragment.newInstance(result))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {
        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}