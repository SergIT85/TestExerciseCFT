package ru.sergsports.androidcource.testexercisecft.data.roomdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sergsports.androidcource.testexercisecft.data.dto.Country

@Entity(tableName = "BinEntity")
data class BinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "number_length")
    val numberLength: Int,

    @ColumnInfo(name = "number_luhn")
    val numberLuhn: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "scheme")
    val scheme: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "brand")
    val brand: String,

    @ColumnInfo(name = "prepaid")
    val prepaid: String,

    @ColumnInfo(name = "bank_name")
    val bankName: String,

    @ColumnInfo(name = "bank_url")
    val bankUrl: String,

    @ColumnInfo(name = "ban_phone")
    val bankPhone: String,

    @ColumnInfo(name = "bank_city")
    val bankCity: String,

)
