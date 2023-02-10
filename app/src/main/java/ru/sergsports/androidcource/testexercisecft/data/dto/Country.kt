package ru.sergsports.androidcource.testexercisecft.data.dto

import ru.sergsports.androidcource.testexercisecft.R

data class Country(
    val numeric: String = R.string.placeholder.toString(),
    val alpha2: String = R.string.placeholder.toString(),
    val name: String = R.string.placeholder.toString(),
    val emoji: String = R.string.placeholder.toString(),
    val currency: String = R.string.placeholder.toString(),
    val latitude: Int = 0,
    val longitude: Int = 0
)
