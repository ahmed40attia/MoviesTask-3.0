package com.example.movies_task30.data.model.MovieImages


data class MovieImagesResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)