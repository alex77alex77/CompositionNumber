package com.alexei.compositionnumber.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexei.compositionnumber.R
import com.alexei.compositionnumber.databinding.FragmentChooseLevelBinding
import com.alexei.compositionnumber.domain.entity.Level

class ChooseLevelFragment : Fragment() {
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonListeners()
    }

    private fun buttonListeners() {
        with(binding) {
            btnTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
            btnEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnNormal.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }
            btnHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }

    }

    private fun launchGameFragment(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "ChooseLevelFragment"
        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }
}