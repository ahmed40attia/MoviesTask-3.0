package com.example.movies_task30.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_task30.data.model.MovieImages.MovieImagesResponse
import com.example.movies_task30.data.model.movieActors.ActorsResponse
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.data.model.person.PersonResponse
import com.example.movies_task30.data.servies.ApiSeries
import com.example.movies_task30.util.Constant
import com.example.movies_task30.util.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ViewModelMovieDetails @Inject constructor(val repo:ApiSeries) : ViewModel(){
    private val _movieDetailsLiveData = MutableLiveData<ResponseDetails>()
    val moviesDetailsLiveData: LiveData<ResponseDetails> = _movieDetailsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData


    private val _movieImagesLiveData = MutableLiveData<MovieImagesResponse>()
    val moviesImagesLiveData: LiveData<MovieImagesResponse> = _movieImagesLiveData

    private val _imagesErrorLiveData = MutableLiveData<String>()
    val imagesErrorLiveData: LiveData<String> = _imagesErrorLiveData

    private val _movieActorsLiveData = MutableLiveData<ActorsResponse>()
    val moviesActorsLiveData: LiveData<ActorsResponse> = _movieActorsLiveData

    private val _actorsErrorLiveData = MutableLiveData<String>()
    val actorsErrorLiveData: LiveData<String> = _actorsErrorLiveData

    private val _personLiveData = MutableLiveData<PersonResponse>()
    val personLiveData: LiveData<PersonResponse> = _personLiveData

    private val _personErrorLiveData = MutableLiveData<String>()
    val personErrorLiveData: LiveData<String> = _personErrorLiveData


    fun getDetails (movie_id:Int){
        viewModelScope.launch {
            try {
                val res = repo.getMovieDetails(api_key = Constant.API_KEY , movie_id = movie_id )
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
                val res = repo.getMovieImages(api_key = Constant.API_KEY , movie_id = movie_id )
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
    fun getActors (movie_id:Int){
        viewModelScope.launch {
            try {
                val res = repo.getMovieActors(api_key = Constant.API_KEY , movie_id = movie_id )
                if (res.isSuccessful)
                    _movieActorsLiveData.postValue(res.body())
                else
                    _actorsErrorLiveData.postValue(res.errorBody()?.getError().toString())
            }catch (e : IOException){
                e.printStackTrace()
                _actorsErrorLiveData.postValue(e.message.toString())
            } catch (e: HttpException) {
                e.printStackTrace()
                _actorsErrorLiveData.postValue(e.response()?.errorBody()?.getError().toString())
            } catch (e: Exception) {
                e.printStackTrace()
                _actorsErrorLiveData.postValue(e.message.toString())
            }
        }
    }
    fun getPerson (person_id:Int){
        viewModelScope.launch {
            try {
                val res = repo.getPerson(api_key = Constant.API_KEY , person_id =  person_id )
                if (res.isSuccessful)
                    _personLiveData.postValue(res.body())
                else
                    _personErrorLiveData.postValue(res.errorBody()?.getError().toString())
            }catch (e : IOException){
                e.printStackTrace()
                _personErrorLiveData.postValue(e.message.toString())
            } catch (e: HttpException) {
                e.printStackTrace()
                _personErrorLiveData.postValue(e.response()?.errorBody()?.getError().toString())
            } catch (e: Exception) {
                e.printStackTrace()
                _personErrorLiveData.postValue(e.message.toString())
            }
        }
    }

}