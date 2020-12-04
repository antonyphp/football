package com.tinybrainz.football.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.tinybrainz.football.repository.TeamsRepository

class TeamListViewModel @ViewModelInject constructor(private val repository: TeamsRepository) : ViewModel() {

    val users by lazy { repository.observeTeamsList() }

}