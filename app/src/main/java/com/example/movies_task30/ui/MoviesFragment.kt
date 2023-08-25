package com.example.movies_task30.ui


import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movies_task30.R
import com.example.movies_task30.data.model.MovieResult
import com.example.movies_task30.databinding.FragmentMoviesBinding
import com.example.movies_task30.listener.MovieListener
import com.example.movies_task30.ui.adapter.MovieAdapter
import com.example.movies_task30.ui.viewModel.ViewModelMovies
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies) , MovieListener {

    private val viewModel: ViewModelMovies by viewModels()
    private lateinit var movieAdapter:MovieAdapter
    private val genreMap = mutableMapOf<String,Int>()
    private var currentCategory:Int = 28



    override fun setUp() {
        observer()
    }

    fun observer (){
        onProgress()

        viewModel.getMovies(currentCategory)
        viewModel.moviesLiveData.observe(viewLifecycleOwner){response ->
            setUpRecycleView(response.results)
            offProgress()
            binding.mainRecycle.visibility = View.VISIBLE
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner){response ->
            Toast.makeText(requireContext() , response , Toast.LENGTH_SHORT).show()
        }

        viewModel.genreMoviesLiveData.observe(viewLifecycleOwner){response ->
                val listGenre = response.genres.map { index ->
                    index.name
                }

            setUpTab(listGenre)

                for (index in response.genres){
                    genreMap[index.name] = index.id
                }

        }

        viewModel.genreErrorLiveData.observe(viewLifecycleOwner){message ->
            Toast.makeText(requireContext() ,message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setUpRecycleView (list:List<MovieResult>){
        val adapter = MovieAdapter(list , this)
        movieAdapter = adapter
        binding.mainRecycle.adapter = movieAdapter

    }

    private fun setUpTab (listGenre:List<String>){

        for (index in listGenre){
            binding.tab.addTab(binding.tab.newTab().setText(index))
        }

        if (binding.tab.tabCount == 2)
            binding.tab.tabMode = TabLayout.MODE_FIXED;
        else
            binding.tab.tabMode = TabLayout.MODE_SCROLLABLE;


        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categoryId:Int? = getDataCategoriesIdByName(tab?.text as String)
                viewModel.getMovies(categoryId as Int)
                currentCategory = categoryId
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    fun getDataCategoriesIdByName(category_name:String) : Int? {
        binding.mainRecycle.visibility = View.GONE
        onProgress()
        val category_id: Int? = genreMap[category_name]
        return category_id
    }

    private fun onProgress(){
        binding.isLoadingMorePages = true
    }

    fun offProgress(){
        binding.isLoadingMorePages = false
    }

    override fun onMovieClick(movieId: Int) {
        callBacks(movieId)
    }

    private fun callBacks (movie_id: Int){
        navController = findNavController()
        val action = MoviesFragmentDirections.actionPopularFragmentToDetailsFragment(movie_id)
        navController.navigate(action)
    }


}