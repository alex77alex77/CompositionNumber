package com.alexei.compositionnumber.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.alexei.compositionnumber.R
import com.alexei.compositionnumber.databinding.FragmentGameFinishedBinding
import com.alexei.compositionnumber.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

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
                gameResult.gameSettings.minCountOfRightAnswers,
                gameResult.gameSettings.minPercentOfRightAnswers,
                getPercentOfRightAnswer(),
                gameResult.countOfRightAnswer
            )

        }
    }

    private fun getSmileResId(): Int {
        return  if (gameResult.winner) {
            R.drawable.emo3_
        } else {
            R.drawable.emo_2
        }
    }

    private fun getPercentOfRightAnswer() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswer / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun setupClickListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    restartGame()
                }
            })

        binding.butGameRestart.setOnClickListener {
            restartGame()
        }
    }

    private fun parseArgs() {

        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun restartGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val KEY_GAME_RESULT = "result"
        fun newInstance(result: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, result)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}