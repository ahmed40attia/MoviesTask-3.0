package com.example.movies_task30.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.movies_task30.R
import com.example.movies_task30.data.model.MovieResult
import com.example.movies_task30.data.model.movieDetails.Genre
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.databinding.FragmentMoviesBinding
import com.example.movies_task30.listener.MovieListener
import com.example.movies_task30.ui.adapter.MovieAdapter
import com.example.movies_task30.ui.viewModel.ViewModelMovieDetails
import com.example.movies_task30.ui.viewModel.ViewModelMovies
import com.google.android.material.tabs.TabLayout


private const val ARG_PARAM1 = "param1"

class MoviesFragment : Fragment() , MovieListener {

    private lateinit var binding : FragmentMoviesBinding
    private val viewModel: ViewModelMovies by viewModels()
    private lateinit var navController:NavController
    lateinit var movieAdapter:MovieAdapter
    private val genreMap = mutableMapOf<String,Int>()

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_movies , container ,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        setUpTab()

    }




    fun observer (){
        binding.isLoadingMorePages = true

        viewModel.getMovies(28)
        viewModel.moviesLiveData.observe(viewLifecycleOwner){response ->
            setUpRecycleView(response.results)
            binding.isLoadingMorePages = false
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner){response ->
            Toast.makeText(requireContext() , response , Toast.LENGTH_SHORT).show()
        }

        viewModel.genreMoviesLiveData.observe(viewLifecycleOwner){response ->
                val listGenre = response.genres.map { index ->
                    index.name
                }

                for (index in response.genres){
                    genreMap[index.name] = index.id
                }

        }

        viewModel.genreErrorLiveData.observe(viewLifecycleOwner){message ->
            Toast.makeText(requireContext() ,message, Toast.LENGTH_SHORT).show()
        }
    }


    fun callBacks (movie_id: Int){
        navController = findNavController()
        val action = MoviesFragmentDirections.actionPopularFragmentToDetailsFragment(movie_id , 28)
        navController.navigate(action)
    }

    fun setUpRecycleView (list:List<MovieResult>){
        movieAdapter = MovieAdapter(list , this)
        binding.mainRecycle.adapter = movieAdapter
    }


    override fun onMovieClick(movieId: Int) {
        callBacks(movieId)
    }

    fun setUpTab (){
        binding.tab.addTab(binding.tab.newTab().setText("Action"))
        binding.tab.addTab(binding.tab.newTab().setText("Adventure"))
        binding.tab.addTab(binding.tab.newTab().setText("Horror"))
        binding.tab.addTab(binding.tab.newTab().setText("Horror"))
        binding.tab.addTab(binding.tab.newTab().setText("Horror"))
        binding.tab.addTab(binding.tab.newTab().setText("Horror"))
        binding.tab.addTab(binding.tab.newTab().setText("Horror"))

        if (binding.tab.tabCount == 2)
            binding.tab.tabMode = TabLayout.MODE_FIXED;
        else
            binding.tab.tabMode = TabLayout.MODE_SCROLLABLE;


        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val cate_id:Int? = get_data_categories_id_by_name(tab?.text as String)
                viewModel.getMovies(cate_id as Int)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }


    fun get_data_categories_id_by_name(category_name:String) : Int? {
        val category = getCategories()
        val category_id: Int? = category[category_name]
        return category_id
    }

    fun getCategories() : MutableMap<String , Int>{
        val categories = mutableMapOf<String , Int>()
        categories["Action"] = 28
        categories["Horror"] = 27
        categories["Adventure"] = 12
        return categories
    }



}