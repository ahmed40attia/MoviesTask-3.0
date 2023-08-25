package com.example.movies_task30.data.servies

import com.example.movies_task30.data.model.MovieImages.MovieImagesResponse
import com.example.movies_task30.data.model.ResponsePopular
import com.example.movies_task30.data.model.movieActors.ActorsResponse
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.data.model.movieGenre.GenreResponse
import com.example.movies_task30.data.model.person.PersonResponse
import retrofit2.Response
import javax.inject.Inject

class ApiServiceImp @Inject constructor(private val apiSeries: ApiSeries) : ApiSeries{
    override suspend fun getMovies(
        with_genres: Int,
        api_key: String,
    ): Response<ResponsePopular> {
        return getMovies(with_genres , api_key)
    }

    override suspend fun getMovieDetails(
        movie_id: Int,
        api_key: String
    ): Response<ResponseDetails> {
        return apiSeries.getMovieDetails(movie_id ,api_key)
    }

    override suspend fun getMovieImages(
        movie_id: Int,
        api_key: String
    ): Response<MovieImagesResponse> {
        return apiSeries.getMovieImages(movie_id ,api_key)
    }

    override suspend fun getMovieGenre(api_key: String): Response<GenreResponse> {
        return apiSeries.getMovieGenre(api_key)
    }

    override suspend fun getMovieActors(movie_id: Int, api_key: String): Response<ActorsResponse> {
        return apiSeries.getMovieActors(movie_id , api_key)
    }

    override suspend fun getSimilar(movie_id: Int , api_key: String ): Response<ResponsePopular> {
        return apiSeries.getSimilar(api_key = api_key , movie_id = movie_id)
    }

    override suspend fun getPerson(person_id: Int, api_key: String): Response<PersonResponse> {
        return apiSeries.getPerson(api_key = api_key , person_id = person_id)
    }

}