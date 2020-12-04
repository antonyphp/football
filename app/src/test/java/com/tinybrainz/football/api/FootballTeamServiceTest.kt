package com.tinybrainz.football.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tinybrainz.football.model.Team
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class FootballTeamServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: FootballTeamsService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FootballTeamsService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }


    @Test
    fun getUser() {
        runBlocking {
            enqueueResponse("team_arsenal_fc.json")
            val teamArsenalFc : Team = service.getTeamDetails("1").body()!!

            val request = mockWebServer.takeRequest()
            assertThat(request.path, `is`("/teams/1/team.json"))

            assertThat(teamArsenalFc, IsNull.notNullValue())
            assertThat(teamArsenalFc.name, `is` ("Arsenal FC"))
            assertThat(
                teamArsenalFc.badgeUrl,
                `is`("/teams/1/badge.png")
            )
        }
    }

    @Test
    fun getUsers() {
        runBlocking {
            enqueueResponse("teams.json")
            val users = service.getTeams().body()!!

            val request = mockWebServer.takeRequest()
            assertThat(request.path, `is`("/teams/teams.json"))

            val team1 = users[0]
            assertThat(team1.teamId, `is` ("1"))
        }
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
        val source = (inputStream?.source())?.buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        if (source != null) {
            mockWebServer.enqueue(mockResponse.setBody(source.readString(Charsets.UTF_8)))
        }
    }

}


