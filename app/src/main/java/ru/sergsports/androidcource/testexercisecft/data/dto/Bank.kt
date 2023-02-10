package ru.sergsports.androidcource.testexercisecft.data.dto

import ru.sergsports.androidcource.testexercisecft.R

data class Bank(
    val name: String = R.string.placeholder.toString(),
    val url: String = R.string.placeholder.toString(),
    val phone: String = R.string.placeholder.toString(),
    val city: String = R.string.placeholder.toString()
)
