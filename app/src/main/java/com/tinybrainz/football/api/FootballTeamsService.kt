package com.tinybrainz.football.api

import com.tinybrainz.football.model.Team
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballTeamsService {

    companion object {
        const val BASE_URL = "https://android-exam.s3-eu-west-1.amazonaws.com"
    }

    @GET("/teams/teams.json")
    suspend fun getTeams(): Response<List<Team>>


    @GET("/teams/{id}/team.json")
    suspend fun getTeamDetails(@Path("id") id: String): Response<Team>
}