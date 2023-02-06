package ru.sergsports.androidcource.testexercisecft.data.dto

data class BinResult(
    val number: Numbers,
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean,
    val country: Country,
    val bank: Bank
)
