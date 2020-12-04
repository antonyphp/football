package com.tinybrainz.football.viewmodels

import com.tinybrainz.football.OpenForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tinybrainz.football.repository.TeamsRepository

@OpenForTesting
class TeamDetailsViewModel @ViewModelInject constructor(private val repository: TeamsRepository) : ViewModel() {
    private val _teamId = MutableLiveData<String>()

    val teamDetails = Transformations
        .switchMap(_teamId) { input ->
        if (input.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.observeTeamDetails(input)
        }
    }

    fun setTeamId(login: String?) {
        _teamId.value = login
    }
}

class AbsentLiveData<T : Any?> private constructor(): LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
