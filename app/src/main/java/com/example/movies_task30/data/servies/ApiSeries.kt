package com.example.movies_task30.data.servies

import com.example.movies_task30.data.model.MovieImages.MovieImagesResponse
import com.example.movies_task30.data.model.ResponsePopular
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.data.model.movieGenre.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSeries {

    //@GET("3/movie/popular")

    @GET("3/discover/movie")
    suspend fun getMovies (
        @Query("with_genres") with_genres:Int,
        @Query("api_key") api_key:String,
        @Query("page") page:Int = 1
    ): Response<ResponsePopular>

    @GET("3/movie/{movie_id}?")
    suspend fun getMovieDetails (
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key:String
    ) : Response<ResponseDetails>

    @GET("3/movie/{movie_id}?/images")
    suspend fun getMovieImages(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key:String
    ) : Response<MovieImagesResponse>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenre(
        @Query("api_key") api_key:String
    ) : Response<GenreResponse>

}