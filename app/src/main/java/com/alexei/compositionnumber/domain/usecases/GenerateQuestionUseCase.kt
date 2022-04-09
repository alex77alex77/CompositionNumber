package com.alexei.compositionnumber.domain.usecases

import com.alexei.compositionnumber.domain.entity.Question
import com.alexei.compositionnumber.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {
    private companion object {
        private const val COUNT_OF_OPTIONS = 6//количество вариантов ответа
    }

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }
}