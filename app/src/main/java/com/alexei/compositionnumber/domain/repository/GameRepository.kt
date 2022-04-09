package com.alexei.compositionnumber.domain.repository

import com.alexei.compositionnumber.domain.entity.GameSettings
import com.alexei.compositionnumber.domain.entity.Level
import com.alexei.compositionnumber.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,//максимальное значение которо необходимо сгенерировать в поле сумма
        countOfOptions: Int//нужно знать количество вариантов ответов
    ): Question //возвращает вопрос

    fun getGameSettings(levels: Level): GameSettings//возвращает настройки игры в зависимости от уровня
}