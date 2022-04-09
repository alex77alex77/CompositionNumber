package com.alexei.compositionnumber.data

import com.alexei.compositionnumber.domain.entity.GameSettings
import com.alexei.compositionnumber.domain.entity.Level
import com.alexei.compositionnumber.domain.entity.Question
import com.alexei.compositionnumber.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {

        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)//----генерация суммы
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)//----генерация видимого числа
        val options = HashSet<Int>()//---в этой коллекции все варианты ответов(HashSet - если сгенерированное число уже есть в коллекции то оно небудет добавленно)
        val rightAnswer = sum - visibleNumber//-----------правельный ответ
        options.add(rightAnswer)

        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer - countOfOptions)
        while (options.size < countOfOptions) { //генерация возможных ответов
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(10, 10, 70, 80)
            }
            Level.EASY -> {
                GameSettings(10, 10, 60, 50)
            }
            Level.NORMAL -> {
                GameSettings(20, 20, 80, 50)
            }
            Level.HARD -> {
                GameSettings(10, 10, 90, 20)
            }
        }
    }
}