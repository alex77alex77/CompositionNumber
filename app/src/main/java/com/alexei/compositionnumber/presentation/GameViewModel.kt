package com.alexei.compositionnumber.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.alexei.compositionnumber.R
import com.alexei.compositionnumber.data.GameRepositoryImpl
import com.alexei.compositionnumber.domain.entity.GameResult
import com.alexei.compositionnumber.domain.entity.GameSettings
import com.alexei.compositionnumber.domain.entity.Level
import com.alexei.compositionnumber.domain.entity.Question
import com.alexei.compositionnumber.domain.usecases.GenerateQuestionUseCase
import com.alexei.compositionnumber.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private val _textProgressAnswer = MutableLiveData<String>()
    val textProgressAnswer: LiveData<String>
        get() = _textProgressAnswer

    private val _enoughCountOfRightAnswer = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswer: LiveData<Boolean>
        get() = _enoughCountOfRightAnswer

    private val _enoughPercentOfRightAnswer = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswer: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswer

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswer = 0
    private var countOfQuestion = 0

    private var timer: CountDownTimer? = null


    private fun calcPercentOfRightAnswer(): Int {
        if (countOfQuestion == 0) {
            return 0
        }
        return ((countOfRightAnswer / countOfQuestion.toDouble()) * 100).toInt()
    }

    fun startGame(level: Level) {

        getGameSetting(level)
        startTimer()
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calcPercentOfRightAnswer()
        _percentOfRightAnswer.value = percent
        _textProgressAnswer.value = String.format(
            context.resources.getString(R.string.progress_answer),
            countOfRightAnswer,
            gameSettings.minCountOfRightAnswers
        )

        _enoughCountOfRightAnswer.value = countOfRightAnswer >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswer.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    fun chooseAnswer(numAnswer: Int) {
        checkAnswer(numAnswer)
        generateQuestion()
        updateProgress()
    }

    private fun checkAnswer(numAnswer: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (numAnswer == rightAnswer) {
            countOfRightAnswer++
        }
        countOfQuestion++
    }


    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }


    private fun getGameSetting(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTE
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTE)

        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCountOfRightAnswer.value == true && enoughPercentOfRightAnswer.value == true,
            countOfRightAnswer,
            countOfQuestion,
            gameSettings
        )
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}