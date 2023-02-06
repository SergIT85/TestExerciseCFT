package ru.sergsports.androidcource.testexercisecft.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.sergsports.androidcource.testexercisecft.data.dto.BinResult


interface BinApiInterface {

    @GET("{number_card}")
    fun getBinIfo(
        @Path("number_card") numberCard: String
    ): Single<BinResult>
}