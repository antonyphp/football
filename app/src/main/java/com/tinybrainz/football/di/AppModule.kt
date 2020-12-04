package com.tinybrainz.football.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import com.tinybrainz.football.db.FootballTeamDatabase
import com.tinybrainz.football.db.TeamsDao
import com.tinybrainz.football.api.FootballTeamsService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Provides
    fun provideGithuberDatabase(app: Application): FootballTeamDatabase {
        return Room
            .databaseBuilder(app, FootballTeamDatabase::class.java, FootballTeamDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: FootballTeamDatabase): TeamsDao {
        return db.teamDao()
    }

    @Provides
    fun provideConnectivityManager(app: Application): ConnectivityManager {
        return app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideGithuberService(): FootballTeamsService {
        return Retrofit.Builder()
            .baseUrl(FootballTeamsService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FootballTeamsService::class.java)
    }

}