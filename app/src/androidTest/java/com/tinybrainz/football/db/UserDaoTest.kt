package com.tinybrainz.football.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tinybrainz.football.util.TestUtil.createTeam
import com.tinybrainz.football.util.TestUtil.getValue
import junit.framework.Assert.assertNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : FootballTeamDatabaseTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var teamDao: TeamsDao

    private val fcb = createTeam("fcb")
    private val sweden = fcb.copy(teamId = "sweden", name = "Sweden")

    @Before
    fun createDb() {
        teamDao = database.teamDao()

        runBlocking {
            teamDao.insertAll(listOf(fcb, sweden))
        }
    }

    @Test
    fun updateTeam() {
        val loaded = getValue(teamDao.getTeamDetails(fcb.teamId))
        assertNull(loaded.badgeUrl)

        val replacement = fcb.copy(badgeUrl = "foo")
        runBlocking {
            teamDao.update(replacement)
        }

        val loadedReplacement = getValue(teamDao.getTeamDetails(replacement.teamId))
        assertThat(loadedReplacement.badgeUrl, `is`("foo"))
    }

    @Test
    fun getTeam() {
        val sweden = getValue(teamDao.getTeamDetails("sweden"))
        assertThat(sweden.teamId, `is`("sweden"))
    }
}
