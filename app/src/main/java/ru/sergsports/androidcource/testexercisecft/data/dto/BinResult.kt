package ru.sergsports.androidcource.testexercisecft.data.dto

import ru.sergsports.androidcource.testexercisecft.R

data class BinResult(
    val number: Numbers,
    val scheme: String = R.string.placeholder.toString(),
    val type: String = R.string.placeholder.toString(),
    val brand: String = R.string.placeholder.toString(),
    val prepaid: Boolean = false,
    val country: Country,
    val bank: Bank
)
