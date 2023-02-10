package ru.sergsports.androidcource.testexercisecft.data.roomdata

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface BinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(binEntity: BinEntity): Completable

    @Delete
    fun delete(binEntity: BinEntity): Completable

    @Query("SELECT * FROM BinEntity")
    fun getBinEntity(): Single<List<BinEntity>>

    @Query("SELECT * FROM BinEntity WHERE id = :requestId")
    fun requestFromId(requestId: Int): Single<BinEntity>
}