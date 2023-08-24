package com.example.movies_task30.util

import com.example.movies_task30.BuildConfig

class Constant {

    companion object {
        const val  BASE_URL = "https://api.themoviedb.org/"
        const val  BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

        const val API_KEY = BuildConfig.API_KET
        const val HORROR_ID = 27
        const val ACTION_ID = 28
        const val ANIMATION_ID = 16
    }
}