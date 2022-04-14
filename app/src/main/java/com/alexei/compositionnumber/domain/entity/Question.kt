package com.alexei.compositionnumber.domain.entity

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val optionsList: List<Int>
) {
    val rightAnswer: Int
        get() = sum - visibleNumber
}
