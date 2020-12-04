package com.tinybrainz.football.repository

import com.tinybrainz.football.api.BaseDataSource
import com.tinybrainz.football.api.FootballTeamsService
import javax.inject.Inject

class TeamsServiceDataSource @Inject constructor(
    private val footballTeamService: FootballTeamsService
) : BaseDataSource() {

    suspend fun fetchTeamDetails(id: String) = getResult { footballTeamService.getTeamDetails(id) }

    suspend fun fetchTeams() = getResult { footballTeamService.getTeams() }
}