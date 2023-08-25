package com.example.movies_task30.ui


import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_task30.R
import com.example.movies_task30.data.model.MovieResult
import com.example.movies_task30.data.model.movieActors.Cast
import com.example.movies_task30.data.model.movieDetails.ResponseDetails
import com.example.movies_task30.data.model.person.PersonResponse
import com.example.movies_task30.databinding.FragmentDetailsBinding
import com.example.movies_task30.databinding.PersonSheetBinding
import com.example.movies_task30.listener.ActorListener
import com.example.movies_task30.listener.MovieListener
import com.example.movies_task30.ui.adapter.ActorAdapter
import com.example.movies_task30.ui.adapter.MovieAdapter
import com.example.movies_task30.ui.viewModel.ViewModelMovieDetails
import com.example.movies_task30.ui.viewModel.ViewModelMovies
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(R.layout.fragment_details),
    MovieListener ,ActorListener {

    private  val viewModel:ViewModelMovieDetails by viewModels()
    private  val similarViewModel:ViewModelMovies by viewModels()
    private lateinit var adapter: MovieAdapter
    private lateinit var castadapter: ActorAdapter
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var bindingSheet:PersonSheetBinding



    override fun setUp() {
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

        viewModel.getActors(getMovieId())
        viewModel.moviesActorsLiveData.observe(viewLifecycleOwner){response ->
            setUpActorsRecycleView(response.cast)
        }

        viewModel.actorsErrorLiveData.observe(viewLifecycleOwner){massage ->
            Toast.makeText(requireContext() , massage , Toast.LENGTH_SHORT).show()
        }

        viewModel.personLiveData.observe(viewLifecycleOwner){response ->
            setUpDialog(response)

        }

        viewModel.personErrorLiveData.observe(viewLifecycleOwner){massage ->
            Toast.makeText(requireContext() ,massage,Toast.LENGTH_SHORT).show()
        }

    }

    private fun similarObserver (){
        binding.isLoadingMorePages = true
        similarViewModel.getSimilar(getMovieId())
        similarViewModel.similarLiveData.observe(viewLifecycleOwner){response ->
            setUpRecycleView(response.results)
            binding.isLoadingMorePages = false
            setVisibility()
        }

        similarViewModel.similarErrorLiveData.observe(viewLifecycleOwner){response ->
            Toast.makeText(requireContext() , response , Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMovieId () = arguments?.getInt("movie_id" ) as Int

    private fun getMovieCategory () = arguments?.getInt("movie_genres" ) as Int

    private fun setData (movie: ResponseDetails) {
        binding.movie = movie

        if (movie.adult)
            binding.status.text = "+12"
        else
            binding.status.text = "+18 "

        binding.rate.text = movie.vote_average.toString().subSequence(0 ,3)

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

    private fun setUpDialog(person: PersonResponse){
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bindingSheet = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.person_sheet,
            view?.findViewById(R.id.person_sheet_cont),
            false
        )
        bottomSheetDialog?.setContentView(bindingSheet.root)
        bindingSheet.person = person

//        if(person.name.toString().trim().isEmpty())
//                bindingSheet.name.visibility = View.GONE
//
//        if(person.birthday.toString().trim().isEmpty())
//            bindingSheet.birthday.visibility = View.GONE
//
//        if(person.deathday.toString().trim().isEmpty())
//            bindingSheet.deathday.visibility = View.GONE
//
//        if(person.known_for_department.toString().trim().isEmpty())
//            bindingSheet.knownFor.visibility = View.GONE
//
//        if(person.biography.toString().trim().isEmpty())
//            bindingSheet.description.visibility = View.GONE

        bottomSheetDialog!!.show()
    }

    private fun setUpViewPager(){

    }

    private fun setVisibility (){
        binding.divi2.visibility = View.VISIBLE
        binding.divi.visibility = View.VISIBLE
        binding.layMisc.visibility = View.VISIBLE
        binding.similar.visibility = View.VISIBLE
    }

    private fun setUpRecycleView (list:List<MovieResult>){
        adapter = MovieAdapter(list , this)

        binding.recycleView.layoutManager = LinearLayoutManager(context ,
            LinearLayoutManager.HORIZONTAL,false)

        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter
    }
    private fun setUpActorsRecycleView (list:List<Cast>){
        castadapter = ActorAdapter(list , this)
        binding.catRecycle.layoutManager = LinearLayoutManager(context ,
            LinearLayoutManager.HORIZONTAL,false)

        binding.catRecycle.adapter = castadapter
        binding.catRecycle.setHasFixedSize(true)
    }


    override fun onMovieClick(movieId: Int) {
        binding.isLoadingMorePages = true
        viewModel.getDetails(movieId)
        binding.isLoadingMorePages = true
        viewModel.getActors(movieId)
    }

    override fun onActorClick(actorId: Int) {
        viewModel.getPerson(actorId)
    }

    private fun callBacks (){
        binding.backSpace.setOnClickListener {
        val action = DetailsFragmentDirections.actionDetailsFragmentToPopularFragment(getMovieCategory())
        navController.navigate(action)
        }
    }

}