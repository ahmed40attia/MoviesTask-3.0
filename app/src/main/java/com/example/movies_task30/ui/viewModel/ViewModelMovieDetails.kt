package com.example.movies_task30.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_task30.Repository.MovieRepo
import com.example.movies_task30.data.model.MovieImages.MovieImagesResponse
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.util.Constant
import com.example.movies_task30.util.getError
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException



class ViewModelMovieDetails()  : ViewModel(){
    private val _movieDetailsLiveData = MutableLiveData<ResponseDetails>()
    val moviesDetailsLiveData: LiveData<ResponseDetails> = _movieDetailsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData


    private val _movieImagesLiveData = MutableLiveData<MovieImagesResponse>()
    val moviesImagesLiveData: LiveData<MovieImagesResponse> = _movieImagesLiveData

    private val _imagesErrorLiveData = MutableLiveData<String>()
    val imagesErrorLiveData: LiveData<String> = _imagesErrorLiveData


    fun getDetails (movie_id:Int){
        viewModelScope.launch {
            try {
                val res = MovieRepo().getMovieDetails(Constant.API_KEY , movie_id = movie_id )
                if (res.isSuccessful)
                    _movieDetailsLiveData.postValue(res.body())
                else
                    _errorLiveData.postValue(res.errorBody()?.getError().toString())
            }catch (e : IOException){
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

    fun getImages (movie_id:Int){
        viewModelScope.launch {
            try {
                val res = MovieRepo().getMovieImages(apiKey = Constant.API_KEY , movie_id = movie_id )
                if (res.isSuccessful)
                    _movieImagesLiveData.postValue(res.body())
                else
                    _imagesErrorLiveData.postValue(res.errorBody()?.getError().toString())
            }catch (e : IOException){
                e.printStackTrace()
                _imagesErrorLiveData.postValue(e.message.toString())
            } catch (e: HttpException) {
                e.printStackTrace()
                _imagesErrorLiveData.postValue(e.response()?.errorBody()?.getError().toString())
            } catch (e: Exception) {
                e.printStackTrace()
                _imagesErrorLiveData.postValue(e.message.toString())
            }
        }
    }

}