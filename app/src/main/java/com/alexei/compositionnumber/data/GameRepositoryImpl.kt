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

        //(вариант чтобы находился рядом с привельным ответом)
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)//------дапозон для генерации правельных ответов
        val to = min(maxSumValue, rightAnswer + countOfOptions)//------дапозон для генерации правельных ответов

        while (options.size < countOfOptions) { //пока количество сгенерированных ответов не равно определенному количеству -> генерация возможных ответов
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(10, 10, 50, 50)
            }
            Level.EASY -> {
                GameSettings(10, 6, 60, 40)
            }
            Level.NORMAL -> {
                GameSettings(10, 8, 80, 30)
            }
            Level.HARD -> {
                GameSettings(10, 10, 100, 10)
            }
        }
    }
}