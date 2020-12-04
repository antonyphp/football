package com.tinybrainz.football.ui

import androidx.recyclerview.widget.RecyclerView
import com.tinybrains.football.databinding.TeamListItemBinding
import com.tinybrainz.football.model.Team

class TeamViewHolder(private val binding: TeamListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(team: Team, userClickCallback: ((Team) -> Unit)) {
        binding.apply {
            this.team = team
            executePendingBindings()
        }
        binding.root.setOnClickListener { userClickCallback.invoke(team) }
    }
}