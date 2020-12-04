package com.tinybrainz.football.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tinybrains.football.databinding.TeamListItemBinding
import com.tinybrainz.football.model.Team

class TeamListAdapter(private val userClickCallback: ((Team) -> Unit)) : ListAdapter<Team, RecyclerView.ViewHolder>(
    TeamDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TeamViewHolder(TeamListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val team = getItem(position)
        (holder as TeamViewHolder).bind(team, userClickCallback)
    }


    private class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {

        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.teamId == newItem.teamId
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem && oldItem.badgeUrl == newItem.badgeUrl
        }
    }
}
