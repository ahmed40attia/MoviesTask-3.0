package com.example.movies_task30.ui

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_task30.R
import com.example.movies_task30.data.model.MovieImages.Backdrop
import com.example.movies_task30.data.model.MovieResult
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.databinding.FragmentDetailsBinding
import com.example.movies_task30.listener.MovieListener
import com.example.movies_task30.ui.adapter.MovieAdapter
import com.example.movies_task30.ui.viewModel.ViewModelMovieDetails
import com.example.movies_task30.ui.viewModel.ViewModelMovies


class DetailsFragment : Fragment() , MovieListener {
    private lateinit var binding: FragmentDetailsBinding
    private  val viewModel:ViewModelMovieDetails by viewModels()
    private  val similarViewModel:ViewModelMovies by viewModels()
    private lateinit var navController: NavController


    lateinit var adapter: MovieAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        navController = findNavController()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_details , container ,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        similarObserver()
        callBacks()

    }



    private fun observer (){
        binding.isLoadingMorePages = true

        viewModel.getDetails(getMovieId())
        viewModel.moviesDetailsLiveData.observe(viewLifecycleOwner){response ->

            setData(response)
            binding.isLoadingMorePages = false
            setVisibility()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner){massage ->
            Toast.makeText(requireContext() , massage , Toast.LENGTH_SHORT).show()
        }
    }

    private fun similarObserver (){
        binding.isLoadingMorePages = true

        similarViewModel.getMovies(28)
        similarViewModel.moviesLiveData.observe(viewLifecycleOwner){response ->
            setUpRecycleView(response.results)
            binding.isLoadingMorePages = false
            setVisibility()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner){response ->
            Toast.makeText(requireContext() , response , Toast.LENGTH_SHORT).show()
        }
    }



    private fun getMovieId () = arguments?.getInt("movie_id" ) as Int

    private fun getMovieCate () = arguments?.getInt("movie_genres" ) as Int
    
    private fun setUpImagesAdapter(list:List<Backdrop>){
        
    }

    fun setData (movie: ResponseDetails) {
        binding.movie = movie

        if (movie.adult)
            binding.status.text = "+18"
        else
            binding.status.text = "+12 "

        binding.rate.text = movie.vote_average.toString()

        if(movie.genres.size > 0){

            binding.genre.text = movie.genres[0].name
            binding.dot.visibility = View.VISIBLE
            binding.genre.visibility = View.VISIBLE
        }

        if(movie.genres.size > 1){

            binding.genre2.text = movie.genres[1].name
            binding.dot2.visibility = View.VISIBLE
            binding.genre2.visibility = View.VISIBLE
        }

        if(movie.genres.size > 2){
            binding.genre3.text = movie.genres[2].name
            binding.dot3.visibility = View.VISIBLE
            binding.genre3.visibility = View.VISIBLE
        }

    }

    fun setVisibility (){
        binding.divi2.visibility = View.VISIBLE
        binding.divi.visibility = View.VISIBLE
        binding.layMisc.visibility = View.VISIBLE
        binding.similar.visibility = View.VISIBLE
    }


    fun setUpRecycleView (list:List<MovieResult>){
        adapter = MovieAdapter(list , this)

        binding.recycleView.layoutManager = LinearLayoutManager(context ,
            LinearLayoutManager.HORIZONTAL,false)

        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter
    }


    override fun onMovieClick(movieId: Int) {
        binding.isLoadingMorePages = true
        viewModel.getDetails(movieId)
        binding.isLoadingMorePages = true
    }

    fun callBacks (){
        binding.backSpace.setOnClickListener {
        val action = DetailsFragmentDirections.actionDetailsFragmentToPopularFragment()
        navController.navigate(action)
        }
    }


}