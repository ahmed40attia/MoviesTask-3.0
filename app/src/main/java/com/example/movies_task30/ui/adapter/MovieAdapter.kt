package com.example.movies_task30.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_task30.R
import com.example.movies_task30.data.model.MovieResult
import com.example.movies_task30.databinding.MovieItemBinding
import com.example.movies_task30.listener.MovieListener


class MovieAdapter(showList: List<MovieResult> , private val movieLis:MovieListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val showList: List<MovieResult>
    private var layoutInflater: LayoutInflater? = null

    init {
        this.showList = showList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: MovieItemBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.movie_item, parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindTVShow(showList[position])

        holder.itemView.rootView.setOnClickListener {
            movieLis.onMovieClick(showList[position].id)
        }

    }

    override fun getItemCount(): Int {
        return showList.size
    }

    inner class MovieViewHolder(binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: MovieItemBinding

        init {
            this.binding = binding
        }

        fun bindTVShow(movie: MovieResult?) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }
}