package com.sprout.hazaricalculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "player1_score")
    val player1Score: Int,
    @ColumnInfo(name = "player2_score")
    val player2Score: Int,
    @ColumnInfo(name = "player3_score")
    val player3Score: Int,
    @ColumnInfo(name = "player4_score")
    val player4Score: Int
)
