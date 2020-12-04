package com.tinybrainz.football.model

import androidx.room.Entity
import com.squareup.moshi.Json

/**
 * Created by Tejaswee Shah on 11/30/20.
 */

@Entity(primaryKeys = ["teamId"], tableName = "teams")
data class Team(
    @field:Json(name = "id")
    val teamId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String? = null,
    @field:Json(name = "gender")
    val gender: String? = null,
    @field:Json(name = "national")
    val national: Boolean? = null,
    @field:Json(name = "badge_url")
    val badgeUrl: String? = null
)
