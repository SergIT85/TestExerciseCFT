package ru.sergsports.androidcource.testexercisecft.data.roomdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sergsports.androidcource.testexercisecft.R
import ru.sergsports.androidcource.testexercisecft.data.dto.Country

@Entity(tableName = "BinEntity")
data class BinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "number_length", defaultValue = R.string.placeholder.toString())
    val numberLength: Int,

    @ColumnInfo(name = "number_luhn", defaultValue = R.string.placeholder.toString())
    val numberLuhn: String,

    @ColumnInfo(name = "country", defaultValue = R.string.placeholder.toString())
    val country: String,

    @ColumnInfo(name = "latitude", defaultValue = R.string.placeholder.toString())
    val latitude: String,

    @ColumnInfo(name = "longitude", defaultValue = R.string.placeholder.toString())
    val longitude: String,

    @ColumnInfo(name = "scheme", defaultValue = R.string.placeholder.toString())
    val scheme: String,
    @ColumnInfo(name = "type", defaultValue = R.string.placeholder.toString())
    val type: String,
    @ColumnInfo(name = "brand", defaultValue = R.string.placeholder.toString())
    val brand: String,

    @ColumnInfo(name = "prepaid", defaultValue = R.string.placeholder.toString())
    val prepaid: String,

    @ColumnInfo(name = "bank_name", defaultValue = R.string.placeholder.toString())
    val bankName: String,

    @ColumnInfo(name = "bank_url", defaultValue = R.string.placeholder.toString())
    val bankUrl: String,

    @ColumnInfo(name = "ban_phone", defaultValue = R.string.placeholder.toString())
    val bankPhone: String,

    @ColumnInfo(name = "bank_city", defaultValue = R.string.placeholder.toString())
    val bankCity: String,

)
