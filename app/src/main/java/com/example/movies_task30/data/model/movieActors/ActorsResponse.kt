package com.example.movies_task30.data.model.movieActors

data class ActorsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)