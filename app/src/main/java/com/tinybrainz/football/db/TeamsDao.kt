package com.tinybrainz.football.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tinybrainz.football.model.Team

@Dao
interface TeamsDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(team: Team)

    @Query("SELECT * FROM teams ORDER BY teamId")
    fun getTeams(): LiveData<List<Team>>


    @Query("SELECT * FROM teams WHERE teamId = :id")
    fun getTeamDetails(id: String): LiveData<Team>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<Team>)
}
