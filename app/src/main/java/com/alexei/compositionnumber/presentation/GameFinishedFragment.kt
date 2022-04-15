package com.alexei.compositionnumber.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexei.compositionnumber.R
import com.alexei.compositionnumber.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    val args by navArgs<GameFinishedFragmentArgs>()
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        bindViews()
    }

    private fun bindViews() {
        with(binding) {

            ivFinished.setImageResource(getSmileResId())

            tvGameResult.text = String.format(
                getString(R.string.debug_result),
                args.result.gameSettings.minCountOfRightAnswers,
                args.result.gameSettings.minPercentOfRightAnswers,
                getPercentOfRightAnswer(),
                args.result.countOfRightAnswer
            )

        }
    }

    private fun getSmileResId(): Int {
        return if (args.result.winner) {
            R.drawable.emo3_
        } else {
            R.drawable.emo_2
        }
    }

    private fun getPercentOfRightAnswer() = with(args.result) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswer / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun setupClickListeners() {
        binding.butGameRestart.setOnClickListener {
            restartGame()
        }
    }


    private fun restartGame() {
        findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}