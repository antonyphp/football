package com.tinybrainz.football.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tinybrains.football.R
import com.tinybrains.football.databinding.TeamListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import com.tinybrainz.football.model.Result
import com.tinybrainz.football.model.Team
import com.tinybrainz.football.viewmodels.TeamListViewModel

@AndroidEntryPoint
class TeamListFragment : Fragment() {

    private val userListViewModel: TeamListViewModel by viewModels()

    private var adapter = TeamListAdapter { user ->
        findNavController().navigate(TeamListFragmentDirections.showUser(user.teamId))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dateBinding = DataBindingUtil.inflate<TeamListFragmentBinding>(
            inflater,
            R.layout.team_list_fragment,
            container,
            false
        )
        dateBinding.teamsList.adapter = adapter
        dateBinding.teamsList.layoutManager = LinearLayoutManager(context)
        dateBinding.teamsList.adapter

        subscribeUi(dateBinding)

        return dateBinding.root
    }

    private fun subscribeUi(dateBinding: TeamListFragmentBinding) {
        userListViewModel.users.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    dateBinding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data ?: emptyList<Team>())
                }
                Result.Status.LOADING -> dateBinding.progressBar.visibility = View.VISIBLE
                Result.Status.ERROR -> {
                    dateBinding.progressBar.visibility = View.GONE
                    Snackbar.make(dateBinding.container, result.message!!, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        })
    }
}