package com.tinybrainz.football.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tinybrainz.football.model.Team
import com.tinybrainz.football.repository.TeamsRepository
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class TeamDetailsViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val teamsRepository = mock(TeamsRepository::class.java)
    private val teamsViewModel = TeamDetailsViewModel(teamsRepository)

    @Test
    fun testNull() {
        assertThat(teamsViewModel.teamDetails, notNullValue())
        verify(teamsRepository, never()).observeTeamDetails(anyString())

        teamsViewModel.setTeamId("1")
        verify(teamsRepository, never()).observeTeamDetails("1")
    }

    @Test
    fun sendResultToUI() {
        val foo = MutableLiveData<com.tinybrainz.football.model.Result<Team>>()
        val bar = MutableLiveData<com.tinybrainz.football.model.Result<Team>>()

        `when`(teamsRepository.observeTeamDetails("foo")).thenReturn(foo)
        `when`(teamsRepository.observeTeamDetails("bar")).thenReturn(bar)

        val observer = mock<Observer<com.tinybrainz.football.model.Result<Team>>>()
        teamsViewModel.teamDetails.observeForever(observer)
        teamsViewModel.setTeamId("foo")
        verify(observer, never()).onChanged(any())

        val fooUser = createUser("foo")
        val fooValue = com.tinybrainz.football.model.Result.success(fooUser)

        foo.value = fooValue
        verify(observer).onChanged(fooValue)
        reset(observer)
        val barUser = createUser("bar")
        val barValue = com.tinybrainz.football.model.Result.success(barUser)
        bar.value = barValue
        teamsViewModel.setTeamId("bar")
        verify(observer).onChanged(barValue)
    }

    @Test
    fun nullUser() {
        val observer = mock<Observer<com.tinybrainz.football.model.Result<Team>>>()
        teamsViewModel.setTeamId("foo")
        teamsViewModel.setTeamId(null)
        teamsViewModel.teamDetails.observeForever(observer)
        verify(observer).onChanged(null)
    }

    private fun createUser(teamId: String) = Team(
        teamId = teamId,
        badgeUrl = null,
        name = "$teamId name",
        gender = null,
        national = null,
        description = null
    )

    private inline fun <reified T> mock(): T = mock(T::class.java)
}