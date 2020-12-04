package com.tinybrainz.football.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tinybrains.football.R
import com.tinybrainz.football.api.FootballTeamsService
import com.tinybrainz.football.model.Team


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, teamData: Team) {
    val fallbackDrawable = when (teamData.gender) {
        "male" ->
            R.drawable.ic_boy
        "female" ->
            R.drawable.ic_girl
        else -> R.drawable.ic_flag
    }
        val avatarUrl = teamData.badgeUrl?.let {
            FootballTeamsService.BASE_URL.plus(teamData.badgeUrl)
        }
        Glide.with(view.context)
            .load(avatarUrl)
            .circleCrop()
            .fitCenter()
            .fallback(fallbackDrawable)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
}