package com.tinybrainz.football.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tinybrains.football.R
import com.tinybrains.football.databinding.TeamDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import com.tinybrainz.football.api.FootballTeamsService
import com.tinybrainz.football.binding.bindImageFromUrl
import com.tinybrainz.football.model.Result
import com.tinybrainz.football.model.Team
import com.tinybrainz.football.viewmodels.TeamDetailsViewModel
import java.util.*

@AndroidEntryPoint
class TeamDetailsFragment : Fragment() {


    private val detailsViewModel: TeamDetailsViewModel by viewModels()

    private val args: TeamDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dataBinding = DataBindingUtil.inflate<TeamDetailsFragmentBinding>(
            inflater,
            R.layout.team_details_fragment,
            container,
            false
        )
        detailsViewModel.setTeamId(args.teamId)

        subscribeUi(dataBinding)

        return dataBinding.root
    }

    private fun subscribeUi(binding: TeamDetailsFragmentBinding) {
        detailsViewModel.teamDetails.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    result.data?.let { bindView(binding, it) }
                }
                Result.Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                Result.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE

                    val message = result.message ?: getString(R.string.unknown_error)
                    Snackbar.make(binding.container, message, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun bindView(binding: TeamDetailsFragmentBinding, team: Team) {
        team.apply {
            bindImageFromUrl(binding.avatar, this)
            binding.name.text = name
            binding.gender.text = gender?.toUpperCase(Locale.getDefault())
            binding.description.text = description
        }
    }
}
