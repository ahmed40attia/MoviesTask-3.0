package com.example.movies_task30.data.model

data class ResponsePopular(
    val page: Int,
    val results: List<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)