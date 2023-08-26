package com.example.movies_task30.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
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
import com.example.movies_task30.ui.adapter.ImageSliderAdapter
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
        recommendationObserver()
        callBacks()
    }

    override fun observer (){
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

        viewModel.getImages(getMovieId())
        viewModel.moviesImagesLiveData.observe(viewLifecycleOwner){response ->

            val images = response.backdrops.map { index ->
                index.file_path
            }

            initialSlider(images)
        }
        viewModel.imagesErrorLiveData.observe(viewLifecycleOwner){massage ->
            Toast.makeText(requireContext() , massage ,Toast.LENGTH_SHORT).show()
        }

    }

    private fun recommendationObserver (){
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

        bottomSheetDialog!!.show()
    }


    private fun initialSlider(images: List<String>) {

        binding.sliderPages.offscreenPageLimit = 1
        binding.sliderPages.adapter = ImageSliderAdapter(images)
        binding.sliderPages.visibility = ViewPager.VISIBLE
        initialSliderIndicator(images.size)
        binding.sliderPages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                highLight_position_slider(position)
            }
        })
    }

    private fun initialSliderIndicator(count: Int) {
        val views = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.leftMargin = 8
        layoutParams.rightMargin = 8

        for (index in views.indices) {
            views[index] = ImageView(requireContext())
            views[index]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.backgroung_slider_indicator
                )
            )
            views[index]!!.layoutParams = layoutParams
            binding.layoutIndicator.addView(views[index])
        }
        binding.layoutIndicator.visibility = View.VISIBLE
        highLight_position_slider(0)
    }


    private fun highLight_position_slider(position: Int) {
        val count: Int = binding.layoutIndicator.childCount
        for (index in 0 until count) {
            val imageView = binding.layoutIndicator.getChildAt(index) as ImageView
            if (index == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.backgroung_slider_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.backgroung_slider_indicator
                    )
                )
            }
        }
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
        viewModel.getActors(movieId)
        viewModel.getImages(movieId)
        binding.isLoadingMorePages = false
    }

    override fun onActorClick(actorId: Int) {
        viewModel.getPerson(actorId)
    }

    private fun callBacks (){
        binding.backSpace.setOnClickListener {
        val action = DetailsFragmentDirections.actionDetailsFragmentToPopularFragment()
        navController.navigate(action)
        }
    }

}