package com.example.movies_task30.Repository

import com.example.movies_task30.data.servies.ApiSeries
import com.example.movies_task30.util.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepo {

    private val apiInterface = RetrofitHelper.getInstance().create(ApiSeries::class.java)

    suspend fun getMovies (apiKey:String, with_genres:Int)
                = apiInterface.getMovies(api_key = apiKey , with_genres = with_genres)

    suspend fun getMovieDetails (apiKey:String , movie_id:Int )
                = apiInterface.getMovieDetails(api_key = apiKey , movie_id = movie_id)

    suspend fun getMovieImages (movie_id: Int , apiKey: String)
                = apiInterface.getMovieImages(api_key = apiKey,movie_id = movie_id)

    suspend fun getMovieGenre (apiKey: String)
                = apiInterface.getMovieGenre(api_key = apiKey)

}

object RetrofitHelper {

    private const val baseUrl = Constant.BASE_URL

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}