package com.tinybrainz.football.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tinybrainz.football.model.Team


@Database(entities = [Team::class], version = 1, exportSchema = false)
abstract class FootballTeamDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "footballteams"
    }

    abstract fun teamDao(): TeamsDao
}