package com.tinybrainz.football.repository

import com.tinybrainz.football.OpenForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.tinybrainz.football.db.TeamsDao
import com.tinybrainz.football.model.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@OpenForTesting
class TeamsRepository
@Inject constructor(
    private val remoteDataSource: TeamsServiceDataSource,
    private val teamDao: TeamsDao
) {

    fun observeTeamDetails(id: String) = resultLiveData(
        databaseQuery = { teamDao.getTeamDetails(id) },
        networkCall = { remoteDataSource.fetchTeamDetails(id) },
        saveCallResult = { teamDao.update(it) })
        .distinctUntilChanged()

    fun observeTeamsList() = resultLiveData(
        databaseQuery = { teamDao.getTeams() },
        networkCall = { remoteDataSource.fetchTeams() },
        saveCallResult = { teamDao.insertAll(it) })
        .distinctUntilChanged()

    private fun <T, A> resultLiveData(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Result<A>,
        saveCallResult: suspend (A) -> Unit
    ): LiveData<Result<T>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading())
            val source = databaseQuery.invoke().map { Result.success(it) }
            emitSource(source)

            val responseStatus = networkCall.invoke()
            if (responseStatus.status == Result.Status.SUCCESS) {
                saveCallResult(responseStatus.data!!)
            } else if (responseStatus.status == Result.Status.ERROR) {
                emit(Result.error<T>(responseStatus.message!!))
                emitSource(source)
            }
        }
}