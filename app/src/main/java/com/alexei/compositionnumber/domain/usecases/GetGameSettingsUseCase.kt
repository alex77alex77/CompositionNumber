package com.alexei.compositionnumber.domain.usecases

import com.alexei.compositionnumber.domain.entity.GameSettings
import com.alexei.compositionnumber.domain.entity.Level
import com.alexei.compositionnumber.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}