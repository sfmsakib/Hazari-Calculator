package com.sprout.hazaricalculator

import android.media.AsyncPlayer
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScoreDao {

    @Insert
    fun insert(score: Score)

    @Insert
    fun insertAll(vararg scores: Score)

    @Update
    fun update(score: Score)

    @Delete
    fun delete(score: Score)

    @Query("delete from score")
    fun deleteAllScore()

//    @Query("SELECT * FROM score order by id asc")
    @Query("SELECT * FROM score")
    fun getAllScore(): LiveData<List<Score>>

    @Query("SELECT * FROM score limit 1")
    fun getSummery(): LiveData<Score>


    @Query("SELECT sum(player1_score) as one," +
            " sum(player2_score) as two," +
            " sum(player3_score) as three," +
            " sum(player4_score) as four from score")
    fun getSum(): LiveData<Results>

}