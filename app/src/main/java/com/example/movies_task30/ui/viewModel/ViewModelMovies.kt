package com.example.movies_task30.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_task30.Repository.MovieRepo
import com.example.movies_task30.data.model.ResponsePopular
import com.example.movies_task30.data.model.movieGenre.GenreResponse
import com.example.movies_task30.util.Constant
import com.example.movies_task30.util.getError
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.properties.Delegates

class ViewModelMovies : ViewModel() {
    private val _moviesLiveData = MutableLiveData<ResponsePopular>()
    val moviesLiveData: LiveData<ResponsePopular> = _moviesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val _genreMoviesLiveData = MutableLiveData<GenreResponse>()
    val genreMoviesLiveData: LiveData<GenreResponse> = _genreMoviesLiveData

    private val _genreErrorLiveData = MutableLiveData<String>()
    val genreErrorLiveData: LiveData<String> = _genreErrorLiveData



    var generc by Delegates.notNull<Int>()

    init {
        getGenre()
    }


    fun getMovies (with_genres:Int) {
         generc = with_genres

        viewModelScope.launch {
            try {
                val res = MovieRepo().getMovies(Constant.API_KEY , with_genres)
                if (res.isSuccessful)
                    _moviesLiveData.postValue(res.body())
                else
                    _errorLiveData.postValue(res.errorBody()?.getError().toString())
            }catch (e :IOException){
                e.printStackTrace()
                _errorLiveData.postValue(e.message.toString())
            } catch (e: HttpException) {
                e.printStackTrace()
                _errorLiveData.postValue(e.response()?.errorBody()?.getError().toString())
            } catch (e: Exception) {
                e.printStackTrace()
                _errorLiveData.postValue(e.message.toString())
            }
        }
    }

    private fun getGenre (){
        viewModelScope.launch {
            val response = MovieRepo().getMovieGenre(Constant.API_KEY)
            if (response.isSuccessful)
                _genreMoviesLiveData.postValue(response.body())
            else
                _genreErrorLiveData.postValue(response.errorBody()?.getError())
        }
    }


}